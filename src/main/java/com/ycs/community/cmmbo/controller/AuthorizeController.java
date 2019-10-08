package com.ycs.community.cmmbo.controller;

import com.ycs.community.cmmbo.domain.dto.GithubAccessTokenRequestDto;
import com.ycs.community.cmmbo.domain.dto.GithubUserResponseDto;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.cmmbo.provider.GithubProvider;
import com.ycs.community.cmmbo.service.UserService;
import com.ycs.community.spring.context.CmmSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
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
}
