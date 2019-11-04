package com.ycs.community.coobo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class AttachPo extends BasePo {
    private Long id;
    private String name;
    private String realNm;
    private String path;
    private String suffix;
    private String type;
    private String size;
    private String operator;
}
