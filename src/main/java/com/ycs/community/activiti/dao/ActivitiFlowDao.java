package com.ycs.community.activiti.dao;

import com.ycs.community.activiti.domain.po.FlowDef;
import com.ycs.community.activiti.domain.po.FlowMain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ActivitiFlowDao {
    List<FlowDef> qryFlowList(Map<String, Object> paramMap);
    FlowDef qryFlowDefByDeploymentId(String deploymentId);
    FlowDef qryFlowDefByCode(String code);
    int addFlowDef(FlowDef flowDef);
    int updFlowDef(FlowDef flowDef);
    long addFlowMain(FlowMain flowMain);
    int updFlowMain(FlowMain flowMain);
    FlowMain qryFlowMainByTaskId(Long taskId);
    FlowDef qryFlowDefById(Long id);
    FlowMain qryFlowMainById(Long id);
    int delFlowDefByKey(String key);
}
