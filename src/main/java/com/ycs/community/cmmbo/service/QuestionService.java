package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.*;
import com.ycs.community.spring.exception.CustomizeBusinessException;

public interface QuestionService {
    boolean askQuestion(QuestionRequestDto request) throws CustomizeBusinessException;
    boolean delQuestion(Long id);
    boolean updQuestion(QuestionRequestDto request);
    QuestionResponseDto qryQuestion(Long id);
    boolean increaseView(Long id);
    QryQuestionPageResponseDto qryQuestionPage(QryQuestionPageRequestDto request);
}
