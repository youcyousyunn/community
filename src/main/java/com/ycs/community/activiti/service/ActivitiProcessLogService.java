package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.ActivitiProcessLogResponseDto;
import com.ycs.community.activiti.domain.po.ProcessLog;

public interface ActivitiProcessLogService {
    ActivitiProcessLogResponseDto qryOperLog(Long processId);
    boolean addProcessLog(ProcessLog processLog);
}
