package com.ycs.community.activiti.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class FlowDef extends BasePo {
    /**
     * 业务流程定义ID
     */
    private Long id;

    /**
     * 流程编码（流程图的编码）
     */
    private String key;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 状态（0:禁用 1:启用）
     */
    private int state;
}
