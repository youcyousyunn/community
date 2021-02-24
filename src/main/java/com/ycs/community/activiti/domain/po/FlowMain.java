package com.ycs.community.activiti.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class FlowMain extends BasePo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 流程ID(启动流程时生成的)
     */
    private Long flowId;

    /**
     * 过程ID
     */
    private Long processId;

    /**
     * 流程状态(1:正常,0:异常)
     */
    private int state;
}
