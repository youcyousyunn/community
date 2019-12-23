package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.CommentRequestDto;
import com.ycs.community.cmmbo.domain.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto qryCommentsByQuestionId(Long questionId);
    boolean commentQuestionOrAnswer(CommentRequestDto request);
}
