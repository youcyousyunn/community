package com.ycs.community.activiti.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class TaskPo extends BasePo {
    private static final long serialVersionUID = -8594680331408088031L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程定义CODE
     */
    private String flowDefCode;

    /**
     * 流程实例ID
     */
    private String flowId;

    /**
     * 办理人
     */
    private String assignee;

    /**
     * 备注
     */
    private String remark;
}
