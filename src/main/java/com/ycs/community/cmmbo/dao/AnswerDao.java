package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.AnswerPo;
import com.ycs.community.cmmbo.domain.po.CommentPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AnswerDao {
    List<AnswerPo> qryAnswerByQuestionId(Long questionId);
    int answerQuestion(AnswerPo answerPo);
    int increaseAnswer(Map<String, Object> paramMap);
    AnswerPo qryAnswer(Long parentId);
}
