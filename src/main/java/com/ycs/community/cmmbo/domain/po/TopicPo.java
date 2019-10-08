package com.ycs.community.cmmbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class TopicPo extends BasePo {
    private int id;
    private String name;
    private String icon;
    private int order;
}
