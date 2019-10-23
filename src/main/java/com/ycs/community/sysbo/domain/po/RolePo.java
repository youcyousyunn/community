package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class RolePo extends BasePo {
    private Long id;
    private String code;
    private String name;
    private String desc;
}
