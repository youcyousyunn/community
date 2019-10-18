package com.ycs.community.sysbo.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerifyCodeRequestDto implements Serializable {
    private int width;
    private int height;
    private int size;
}
