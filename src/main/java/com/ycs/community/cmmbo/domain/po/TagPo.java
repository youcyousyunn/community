package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.List;

@Data
public class TagPo extends BasePo {
    private Long id;
    private String name;
    private Long pid;
    private String description;
    private String icon;
    private int star;
    private int order;
    private List<TagPo> children;
}
