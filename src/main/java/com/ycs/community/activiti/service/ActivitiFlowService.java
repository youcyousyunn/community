package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.dto.QryActivitiFlowRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiFlowResponseDto;
import com.ycs.community.activiti.domain.po.FlowDef;
import com.ycs.community.activiti.domain.po.FlowMain;
import org.activiti.engine.task.Task;

import java.util.Map;

public interface ActivitiFlowService {
    QryActivitiFlowResponseDto qryFlowList(QryActivitiFlowRequestDto request);
    boolean addFlowDef(FlowDef flowDef);
    String resolve(Long processId, Long flowDefId, Map<String, Object> variables);
    FlowMain qryFlowMainByTaskId(Long taskId);
    Task qryTaskByInstId(String processInstanceId);
    FlowMain qryFlowMainById(Long id);
    FlowDef qryFlowDefByDeploymentId(String deploymentId);
    boolean delFlowDefByKey(String key);
}
