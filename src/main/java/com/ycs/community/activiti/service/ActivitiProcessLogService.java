package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.ActivitiProcessLogResponseDto;

public interface ActivitiProcessLogService {
    ActivitiProcessLogResponseDto qryOperLog(Long processId);
}
