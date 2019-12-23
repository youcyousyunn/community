package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.AnswerRequestDto;
import com.ycs.community.cmmbo.domain.dto.AnswerResponseDto;

public interface AnswerService {
    AnswerResponseDto qryAnswersByQuestionId(Long questionId);
    boolean answerQuestion(AnswerRequestDto request);
}
