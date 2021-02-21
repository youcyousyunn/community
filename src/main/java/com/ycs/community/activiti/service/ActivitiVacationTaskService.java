package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.ActivitiVacationTaskRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiVacationTaskPageResponseDto;

public interface ActivitiVacationTaskService {
    boolean delVacationTaskById(Long id);
    QryActivitiVacationTaskPageResponseDto qryMyVacationTaskPage(QryActivitiVacationTaskPageRequestDto request);
    boolean addVacationTask(ActivitiVacationTaskRequestDto request);
    boolean updVacationTask(ActivitiVacationTaskRequestDto request);
    boolean submitVacationTask(ActivitiVacationTaskRequestDto request);
}
