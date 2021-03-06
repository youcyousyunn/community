package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.CommentPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommentDao {
    List<CommentPo> qryCommentsByQuestionId(Long questionId);
    int commentQuestionOrAnswer(CommentPo commentPo);
    int increaseComment(Map<String, Object> paramMap);
    int delCommentsByQuestionId(Long questionId);
    int delCommentsByAnswerIds(List<Long> answerIds);
}
