package com.ycs.community.activiti.service;

import com.ycs.community.activiti.domain.po.FlowMain;
import org.activiti.engine.task.Task;

import java.util.Map;

public interface ActivitiInfoService {
    String resolve(Long taskId, Map<String, Object> variables);
    String runFlow(FlowMain flowMain, Map<String, Object> variables);
    FlowMain qryFlowMainByTaskId(Long taskId);
    Task qryTaskByInstId(String processInstanceId);
}
