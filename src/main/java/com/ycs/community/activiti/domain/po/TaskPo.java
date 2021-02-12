package com.ycs.community.activiti.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
public class TaskPo implements Serializable {
    private static final long serialVersionUID = -8594680331408088031L;

    /**
     * 请假单ID
     */
    private String vacationId;

    /**
     * 请假人ID
     */
    private String userId;

    /**
     * 请假开始时间
     */
    private LocalDate startTime;

    /**
     * 请假结束时间
     */
    private LocalDate endTime;

    /**
     * 请假原因
     */
    private String vacationContext;

    /**
     * 流程定义ID
     */
    private String flowDefId;

    /**
     * 流程实例ID
     */
    private String flowId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程定义KEY
     */
    private String taskDefKey;

    /**
     * 办理人
     */
    private String assign;

    /**
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 审批类型 0:同意 1:驳回
     */
    private String approvalType;
    /**
     * 备注
     */
    private String remark;
}
