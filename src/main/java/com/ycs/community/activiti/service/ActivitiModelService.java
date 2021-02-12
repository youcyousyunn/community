package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.ActivitiModelRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageResponseDto;

import java.io.IOException;

public interface ActivitiModelService {
    QryActivitiModelPageResponseDto qryActivitiModelPage(QryActivitiModelPageRequestDto request);
    boolean deployModel(ActivitiModelRequestDto request) throws IOException;
}
