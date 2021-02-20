package com.ycs.community.activiti.domain.po;

import lombok.Data;

@Data
public class VacationTaskPo extends TaskPo {

    /**
     * 请假单ID
     */
    private String id;

    /**
     * 请假人ID
     */
    private Long userId;

    /**
     * 请假人姓名
     */
    private String userName;

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
