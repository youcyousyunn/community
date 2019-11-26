package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.JobPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobDao {
    int qryJobCount(Map<String, Object> paramMap);
    List<JobPo> qryJobPage(Map<String, Object> paramMap);
    int addJob(JobPo jobPo);
    int delJob(Long id);
    int updJob(JobPo jobPo);
    List<JobPo> qryJobsByDeptId(Long deptId);
}
