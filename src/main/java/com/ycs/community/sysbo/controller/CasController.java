package com.ycs.community.sysbo.controller;

import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.ArithmeticCaptcha;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.spring.security.domain.po.OnlineUserPo;
import com.ycs.community.spring.security.service.OnlineUserService;
import com.ycs.community.spring.security.utils.JwtTokenUtil;
import com.ycs.community.sysbo.domain.dto.UserRequestDto;
import com.ycs.community.sysbo.domain.dto.UserResponseDto;
import com.ycs.community.sysbo.domain.dto.VerifyCodeRequestDto;
import com.ycs.community.sysbo.domain.dto.VerifyCodeResponseDto;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.domain.po.VerifyCodePo;
import com.ycs.community.sysbo.service.RedisService;
import com.ycs.community.sysbo.service.UserService;
import com.ycs.community.sysbo.utils.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class CasController {
    private static final String COOKIE_USER_TICKET = "cookie_user_ticket";
    private static final String REDIS_TMP_TICKET = "redis_tmp_ticket";

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.online.key}")
    private String onlineKey;

    /**
     * 统一认证中心登录
     * @param request
     * @return
     */
    @AnonymousAccess
    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto request, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }

        // 校验用户状态&密码
        UserPo userPo = (UserPo) userDetailsService.loadUserByUsername(request.getName());
        if (!userPo.isEnabled()) {
            throw new BadRequestException("账号已被停用, 请联系管理员");
        }
        if (!userPo.getPassword().equals(EncryptUtil.encryptPassword(request.getPassword()))) {
            throw new BadRequestException("密码错误");
        }

        // 校验验证码
        String vCode = redisService.qryVCode(Constants.LOGIN_CAPTCHA_PREFIX + "::" + request.getUuid());
        redisService.delVCode(Constants.LOGIN_CAPTCHA_PREFIX + "::" + request.getUuid());
        if (StringUtils.isEmpty(vCode)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (!vCode.equals(request.getCode())) {
            throw new BadRequestException("验证码错误");
        }

        UserResponseDto responseDto = new UserResponseDto();
        List<RolePo> roles = userService.qryRolesByUserId(userPo.getId());
        userPo.setRoles(roles);
        responseDto.setData(userPo);

        // 生成全局令牌
        final String ticket = jwtTokenUtil.generateToken(userPo);

        // CAS端设置全局令牌cookie
        setCookie(COOKIE_USER_TICKET, ticket, response);

        // 保存在线用户信息
        onlineUserService.saveOnlineUserInfo(userPo, ticket, httpServletRequest);

        // 生成临时令牌,回跳到调用端网站验证使用,是CAS端签发的一个一次性令牌
        String tmpTicket = createTmpTicket();

//        responseDto.setToken(ticket);
        String redirectUrl = request.getRedirectUrl() + "?tmpTicket=" + tmpTicket;
        responseDto.setRedirectUrl(redirectUrl);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 获取算术登录验证码
     * @param request
     * @return
     * @throws IOException
     */
    @AnonymousAccess
    @PostMapping("/arithmetic/vCode")
    public VerifyCodeResponseDto getArithmeticVerifyCode (@RequestBody VerifyCodeRequestDto request) {
        int width = request.getWidth();
        int height = request.getHeight();
        int size = request.getSize();
        // 算术验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(width, height);
        // 几位数运算，默认是两位
        captcha.setLen(size);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = UUID.randomUUID().toString();
        // 添加算术验证码到redis缓存
        redisService.addVerifyCode(Constants.LOGIN_CAPTCHA_PREFIX + "::" + uuid, result);
        VerifyCodeResponseDto responseDto = new VerifyCodeResponseDto();
        VerifyCodePo data = new VerifyCodePo(uuid, captcha.toBase64());
        responseDto.setData(data);
        return  responseDto;
    }

    /**
     * 验证临时令牌是否合法
     * @param tmpTicket
     * @param request
     * @param response
     */
    @PostMapping("/valid/ticket")
    public UserResponseDto validTmpTicket(String tmpTicket, HttpServletRequest request, HttpServletResponse response) {
        // 使用一次性临时令牌验证用户是否登录,如果登录过将用户信息返回给站点
        String tmpTicketValue = redisService.qryTmpTicket(REDIS_TMP_TICKET + "::" + tmpTicket);
        if(StringUtils.isBlank(tmpTicketValue) || !tmpTicketValue.equals(tmpTicket)) {
            throw new BadRequestException("临时令牌异常");
        } else { // 如果临时令牌校验通过则销毁门票,并通过拿到CAS端cookie中的全局令牌来获取用户信息
            redisService.delTmpTicket(REDIS_TMP_TICKET + "::" + tmpTicket);
        }

        // 验证全局令牌绑定的用户信息是否存在
        String ticket = getCookie(COOKIE_USER_TICKET, request);
        if(StringUtils.isBlank(ticket)) {
            throw new BadRequestException("CAS令牌异常");
        }

        OnlineUserPo onlineUser = JSONObject.parseObject(redisService.get(onlineKey + ticket), OnlineUserPo.class);
        UserResponseDto responseDto = new UserResponseDto();
        UserPo userPo = new UserPo();
        userPo.setAccountId(onlineUser.getAccountId());
        userPo.setName(onlineUser.getName());
        userPo.setNickname(onlineUser.getNickname());
        userPo.setSex(onlineUser.getSex());
        userPo.setPhone(onlineUser.getPhone());
        userPo.setEmail(onlineUser.getEmail());
        userPo.setAvatar(onlineUser.getAvatar());
        userPo.setRoles(onlineUser.getRoles());
        responseDto.setData(userPo);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 统一认证中心认证
     * @param redirectUrl
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/login")
    public void login(String redirectUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取全局令牌,如果cookie中能够获取到证明该用户已登录
        String ticket = getCookie(COOKIE_USER_TICKET, request);

        // 验证全局令牌是否合法
        boolean isValid = validTicket(ticket);
        if(isValid) {
            // 全局令牌合法, 那么再签发一个临时令牌
            String tmpTicket = createTmpTicket();
            response.sendRedirect(redirectUrl + "?tmpTicket=" + tmpTicket);
        } else {
            // TODO 前后端可以对redirectUrl进行编解码传输
            response.sendRedirect("http://www.cas.com?redirectUrl=" + redirectUrl);
        }
    }

    /**
     * 创建临时令牌
     * @return
     */
    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim().replace("-", "");
        redisService.addTmpTicket(REDIS_TMP_TICKET + "::" + tmpTicket, tmpTicket, 1);
        return tmpTicket;
    }

    /**
     * 验证全局令牌是否合法
     * @param ticket
     * @return
     */
    private boolean validTicket(String ticket) {
        // 验证令牌是否存在
        if(StringUtils.isBlank(ticket)) {
            return false;
        }

        // 验证全局令牌对应的用户信息是否存在
        String userJsonStr = redisService.get(onlineKey + ticket);
        if(StringUtils.isBlank(userJsonStr)) {
            return false;
        }
        return true;
    }

    /**
     * 获取请求中cookie
     * @param request
     * @param name
     * @return
     */
    private String getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(null == cookies || StringUtils.isBlank(name)) {
            return null;
        }

        String cookieValue = null;
        for (Cookie cookie : cookies) {
            if(name.equals(cookie.getName())) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }

    /**
     * 设置Cookie
     * @param name
     * @param value
     * @param response
     */
    private void setCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("cas.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 清除Cookie
     * @param name
     * @param response
     */
    private void clearCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setDomain("cas.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    /**
     * 注销
     * @param ticket
     * @param request
     * @param response
     */
    @PostMapping("logout")
    public UserResponseDto logout(String ticket, HttpServletRequest request, HttpServletResponse response) {
        // 清除全局门票Cookie信息
        clearCookie(COOKIE_USER_TICKET, response);
        // 清除全局门票对应的redis用户信息
        onlineUserService.delOnlineUserInfo(ticket);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
