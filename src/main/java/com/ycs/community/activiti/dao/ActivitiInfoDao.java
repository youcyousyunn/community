package com.ycs.community.activiti.dao;

import com.ycs.community.activiti.domain.po.FlowMain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ActivitiInfoDao {
    long addFlowMain(FlowMain flowMain);
    int updFlowMain(FlowMain flowMain);
    FlowMain qryFlowMainByTaskId(Long taskId);
}
