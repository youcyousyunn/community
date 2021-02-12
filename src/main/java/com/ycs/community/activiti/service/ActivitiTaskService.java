package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageResponseDto;

public interface ActivitiTaskService {
    QryActivitiTaskPageResponseDto queryMyTaskPage(QryActivitiTaskPageRequestDto request);
}
