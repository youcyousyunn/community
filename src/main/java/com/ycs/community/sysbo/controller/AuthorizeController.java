package com.ycs.community.sysbo.controller;

import cn.hutool.core.codec.Base64;
import com.wf.captcha.ArithmeticCaptcha;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.GithubAccessTokenRequestDto;
import com.ycs.community.cmmbo.domain.dto.GithubUserResponseDto;
import com.ycs.community.sysbo.domain.dto.UserRequestDto;
import com.ycs.community.sysbo.domain.dto.UserResponseDto;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.cmmbo.provider.GithubProvider;
import com.ycs.community.sysbo.service.UserService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.spring.security.service.OnlineUserService;
import com.ycs.community.spring.security.utils.JwtTokenUtil;
import com.ycs.community.sysbo.domain.dto.VerifyCodeRequestDto;
import com.ycs.community.sysbo.domain.dto.VerifyCodeResponseDto;
import com.ycs.community.sysbo.domain.po.VerifyCodePo;
import com.ycs.community.sysbo.service.RedisService;
import com.ycs.community.sysbo.utils.EncryptUtil;
import com.ycs.community.sysbo.utils.VerifyCodeUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Api(tags = "用户登录权限认证")
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    /**
     * 登录
     * @param request
     * @return
     */
    @AnonymousAccess
    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto request, HttpServletRequest httpServletRequest) {
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
            throw new BadRequestException("验证码已过期");
        }
        if (!vCode.equalsIgnoreCase(request.getCode())) {
            throw new BadRequestException("验证码错误");
        }

        UserResponseDto responseDto = new UserResponseDto();
        List<RolePo> roles = userService.qryRolesByUserId(userPo.getId());
        userPo.setRoles(roles);
        responseDto.setData(userPo);

        // 生成令牌
        final String token = jwtTokenUtil.generateToken(userPo);
        // 保存在线用户信息
        onlineUserService.saveOnlineUserInfo(userPo, token, httpServletRequest);

        responseDto.setToken(token);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 获取图形登录验证码
     * @param request
     * @return
     */
    @PostMapping("/vCode")
    public VerifyCodeResponseDto getVerifyCode (@RequestBody VerifyCodeRequestDto request) throws IOException {
        VerifyCodeResponseDto responseDto = new VerifyCodeResponseDto();
        // 生成随机字符串
        String code = VerifyCodeUtil.generateVerifyCode(request.getSize());
        // 生成图片
        int width = request.getWidth();
        int height = request.getHeight();
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        try {
            VerifyCodeUtil.outputImage(width, height, ops, code);
            String uuid = UUID.randomUUID().toString();
            VerifyCodePo data = new VerifyCodePo(uuid, Base64.encode(ops.toByteArray()));
            // 添加验证码到redis缓存
            redisService.addVerifyCode(Constants.LOGIN_CAPTCHA_PREFIX + "::" + uuid, code);
            responseDto.setData(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ops.close();
        }
        return  responseDto;
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
     * Github回调函数
     * @param code
     * @param state
     * @param response
     * @return
     */
    @GetMapping("/github/callback")
    public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) {
        GithubAccessTokenRequestDto githubAccessTokenRequestDto = new GithubAccessTokenRequestDto();
        githubAccessTokenRequestDto.setClient_id(clientId);
        githubAccessTokenRequestDto.setClient_secret(clientSecret);
        githubAccessTokenRequestDto.setRedirect_uri(redirectUri);
        githubAccessTokenRequestDto.setCode(code);
        githubAccessTokenRequestDto.setState(state);
        String accessToken = githubProvider.getAccessToken(githubAccessTokenRequestDto);
        GithubUserResponseDto githubUserResponseDto = githubProvider.getUser(accessToken);
        HttpSession session = request.getSession();
        if (!StringUtils.isEmpty(githubUserResponseDto)) {
            UserPo userPo = new UserPo();
            userPo.setToken(session.getId());
            userPo.setName(githubUserResponseDto.getName());
            userPo.setAccountId(githubUserResponseDto.getId());
            userPo.setAvatar(githubUserResponseDto.getAvatar());
            boolean result = userService.addOrUpdateUser(userPo);
            if(result) {
                // 生成令牌
                final String token = jwtTokenUtil.generateToken(userPo);
                // 保存在线用户信息
                onlineUserService.saveOnlineUserInfo(userPo, token, request);
            }
        }

        return "redirect:http://localhost/community?token="+session.getId();
    }

    /**
     * 注销登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    @AnonymousAccess
    public UserResponseDto logout(HttpServletRequest request) {
        onlineUserService.logout(jwtTokenUtil.getToken(request));
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
