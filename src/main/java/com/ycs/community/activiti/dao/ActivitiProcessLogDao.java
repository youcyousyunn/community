package com.ycs.community.activiti.dao;

import com.ycs.community.activiti.domain.po.ProcessLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ActivitiProcessLogDao {
    List<ProcessLog> qryOperLog(Long processId);
}
