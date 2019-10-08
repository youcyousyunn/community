package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.dto.QuestionRequestDto;
import com.ycs.community.cmmbo.domain.po.CommentPo;
import com.ycs.community.cmmbo.domain.po.QuestionPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommentDao {
    List<CommentPo> qryComment(Map<String, Object> paramMap);
    int commentQuestionOrAnswer(CommentPo commentPo);
    int increaseComment(Map<String, Object> paramMap);
}
