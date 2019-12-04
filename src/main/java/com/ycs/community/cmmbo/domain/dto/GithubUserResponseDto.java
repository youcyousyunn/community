package com.ycs.community.cmmbo.domain.dto;

import lombok.Data;

@Data
public class GithubUserResponseDto {
    private Long id;
    private String name;
    private String bio;
    private String avatar;
}
