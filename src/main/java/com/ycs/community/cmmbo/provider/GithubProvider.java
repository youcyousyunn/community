package com.ycs.community.cmmbo.provider;

import com.alibaba.fastjson.JSON;
import com.ycs.community.cmmbo.domain.dto.GithubAccessTokenRequestDto;
import com.ycs.community.cmmbo.domain.dto.GithubUserResponseDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubProvider {

    /**
     * 获取Github令牌
     * @param githubAccessTokenDto
     * @return
     */
    public String getAccessToken(GithubAccessTokenRequestDto githubAccessTokenDto) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(githubAccessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            log.error("getAccessToken error", githubAccessTokenDto, e);
        }
        return null;
    }

    /**
     * 获取Github用户信息
     * @param accessToken
     * @return
     */
    public GithubUserResponseDto getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            GithubUserResponseDto githubUserResponseDto = JSON.parseObject(json, GithubUserResponseDto.class);
            return githubUserResponseDto;
        } catch (Exception e) {
            log.error("getUser error", accessToken, e);
        }
        return null;
    }
}
