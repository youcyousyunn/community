package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class TagPo extends BasePo {
    private long id;
    private String name;
    private long pid;
    private String description;
    private String icon;
    private int star;
    private int order;
}
