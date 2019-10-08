package com.ycs.community.cmmbo.service;

import com.ycs.community.cmmbo.domain.dto.*;
import com.ycs.community.spring.exception.CustomizeBusinessException;

public interface CommentService {
    CommentResponseDto qryComment(Long parentId, int commentType);
    boolean commentQuestionOrAnswer(CommentRequestDto request);
}
