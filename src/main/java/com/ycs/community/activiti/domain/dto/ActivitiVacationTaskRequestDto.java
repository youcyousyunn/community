package com.ycs.community.activiti.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class ActivitiVacationTaskRequestDto extends BaseRequestDto {
    private Long id;
    private int type;
    private String title;
    private String context;
    private Long applierId;
    private Long assigneeId;
    private String applierName;
    private String assigneeName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private int state;
    private Long flowDefId;

    /**
     * 审批类型 agree：同意 reject：驳回
     */
    private String taskId;
    private String taskName;
    private String approvalType;
    private String remark;

    /**
     * 接口请求报文检查
     * @return
     */
    public boolean checkRequestDto() {
        if (StringUtils.isEmpty(title)) {
            return false;
        }
        if (StringUtils.isEmpty(startTime)) {
            return false;
        }
        if (StringUtils.isEmpty(endTime)) {
            return false;
        }
        return true;
    }
}
