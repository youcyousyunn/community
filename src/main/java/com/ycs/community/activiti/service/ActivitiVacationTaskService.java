package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.ActivitiVacationTaskRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageResponseDto;

public interface ActivitiVacationTaskService {
    QryActivitiVacationTaskPageResponseDto queryMyVacationTaskPage(QryActivitiVacationTaskPageRequestDto request);
    boolean addVacationTask(ActivitiVacationTaskRequestDto request);
    boolean updVacationTask(ActivitiVacationTaskRequestDto request);
    boolean submitVacationTask(ActivitiVacationTaskRequestDto request);
}
