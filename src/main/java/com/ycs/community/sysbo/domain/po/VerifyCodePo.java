package com.ycs.community.sysbo.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodePo implements Serializable {
    private String uuid;
    private String img;
}
