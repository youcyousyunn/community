package com.ycs.community.activiti.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BaseTaskPo extends BasePo {
    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务定义key
     */
    private String taskDefKey;

    /**
     * 流程定义ID
     */
    private Long flowDefId;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
