package com.ycs.community.sysbo.dao;

import com.ycs.community.cmmbo.domain.po.QuestionPo;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface QuartzJobDao {
    int addJob(QuartzJobPo quartzJobPo);
    int qryQuartzCount(Map<String, Object> paramMap);
    List<QuartzJobPo> qryQuartzPage(Map<String, Object> paramMap);
    QuartzJobPo qryJobById(Long id);
    List<QuartzJobPo> qryJobByRunning();
    int updJobStatus(QuartzJobPo quartzJobPo);
}
