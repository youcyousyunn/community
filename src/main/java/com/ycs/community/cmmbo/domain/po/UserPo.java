package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserPo extends BasePo {
    private Long id;
    private Long accountId;
    private String name;
    private String password;
    private String token;
    private String avatarUrl;
    private List<String> roles = new ArrayList<>();
}
