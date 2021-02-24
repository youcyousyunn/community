package com.ycs.community.activiti.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

/**
 * 流程操作日志表
 */
@Data
public class ProcessLog extends BasePo {
    private Long id;
    private Long processId;
    private String taskId;
    private String taskKey;
    private String taskName;
    private String approvStatus;
    private Long userId;
    private String operValue;
    private String remark;
    private String userName;
    private String nickname;
    private String avatar;
}
