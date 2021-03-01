package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageResponseDto;
import com.ycs.community.activiti.domain.po.BaseTaskPo;

public interface ActivitiTaskService {
    QryActivitiTaskPageResponseDto queryMyTaskPage(QryActivitiTaskPageRequestDto request);
    BaseTaskPo qryTaskByProcessId(Long processId);
}
