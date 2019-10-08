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
public interface QuestionDao {
    int askQuestion(QuestionPo request);
    int delQuestion(Long id);
    int updQuestion(QuestionRequestDto request);
    QuestionPo qryQuestion(Long id);
    int increaseView(Map<String, Object> paramMap);
    int qryQuestionCount(Map<String, Object> paramMap);
    List<QuestionPo> qryQuestionPage(Map<String, Object> paramMap);
}
