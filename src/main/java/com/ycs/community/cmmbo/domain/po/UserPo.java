package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class UserPo extends BasePo {
    private Long id;
    private Long accountId;
    private String name;
    private String password;
    private String code;
    private String uuid;
    private String token;
    private String avatarUrl;
}
