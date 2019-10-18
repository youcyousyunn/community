package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.sysbo.domain.po.VerifyCodePo;
import lombok.Data;

import java.io.Serializable;

@Data
public class VerifyCodeResponseDto implements Serializable {
    private VerifyCodePo data;
}
