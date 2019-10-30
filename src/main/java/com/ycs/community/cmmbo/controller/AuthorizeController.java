package com.ycs.community.cmmbo.controller;

import cn.hutool.core.codec.Base64;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.cmmbo.domain.dto.GithubAccessTokenRequestDto;
import com.ycs.community.cmmbo.domain.dto.GithubUserResponseDto;
import com.ycs.community.cmmbo.domain.dto.UserRequestDto;
import com.ycs.community.cmmbo.domain.dto.UserResponseDto;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.cmmbo.provider.GithubProvider;
import com.ycs.community.cmmbo.service.UserService;
import com.ycs.community.spring.context.CmmSessionContext;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.VerifyCodeRequestDto;
import com.ycs.community.sysbo.domain.dto.VerifyCodeResponseDto;
import com.ycs.community.sysbo.domain.po.VerifyCodePo;
import com.ycs.community.sysbo.service.RedisService;
import com.ycs.community.sysbo.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
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


    /**
     * 登录
     * @param request
     * @return
     */
    @PostMapping("/login")
    public UserResponseDto login (@RequestBody UserRequestDto request, HttpSession session) throws CustomizeBusinessException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        // 校验验证码
        String vCode = redisService.qryVCode(Constants.LOGIN_CAPTCHA_PREFIX + "::" + request.getUuid());
        if (StringUtils.isEmpty(vCode)) {
            throw new BadRequestException("验证码已过期");
        }
        if (!vCode.equalsIgnoreCase(request.getCode())) {
            throw new BadRequestException("验证码错误");
        }

        UserResponseDto responseDto = new UserResponseDto();
        // 校验密码
        responseDto = userService.login(request);
        CmmSessionContext instance = CmmSessionContext.getInstance();
        session.setAttribute(session.getId().toString(), responseDto.getData().getAccountId());
        instance.addSession(session);
        responseDto.setToken(session.getId().toString());
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 获取登录验证码
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
            userPo.setAvatarUrl(githubUserResponseDto.getAvatarUrl());
            boolean result = userService.addOrUpdateUser(userPo);
            if(result) {
                CmmSessionContext instance = CmmSessionContext.getInstance();
                session.setAttribute(session.getId().toString(), githubUserResponseDto.getId());
                instance.addSession(session);
            }
        }

        return "redirect:http://localhost/community?token="+session.getId();
    }

    /**
     * 注销登录
     * @param sessionId
     * @return
     */
    @GetMapping("/logout/{sessionId}")
    public UserResponseDto logout(@PathVariable("sessionId") String sessionId) {
        UserResponseDto responseDto = new UserResponseDto();
        CmmSessionContext instance = CmmSessionContext.getInstance();
        HttpSession session = instance.getSession(sessionId);
        instance.delSession(session);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
