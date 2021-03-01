package com.ycs.community.activiti.domain.po;

import lombok.Data;

@Data
public class VacationTaskPo extends BaseTaskPo {

    /**
     * 请假单ID
     */
    private Long id;

    /**
     * 申请人ID
     */
    private Long applierId;

    /**
     * 申请人姓名
     */
    private String applierName;

    /**
     * 审批人ID
     */
    private Long assigneeId;

    /**
     * 审批人姓名
     */
    private String assigneeName;

    /**
     * 请假类型
     */
    private int type;

    /**
     * 请假标题
     */
    private String title;

    /**
     * 请假内容
     */
    private String context;

    /**
     * 请假开始时间
     */
    private long startTime;

    /**
     * 请假结束时间
     */
    private long endTime;

    /**
     * 请假状态
     */
    private int state;
}
