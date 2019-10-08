package com.ycs.community.cmmbo.domain.dto;

import lombok.Data;

@Data
public class GithubAccessTokenRequestDto {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
