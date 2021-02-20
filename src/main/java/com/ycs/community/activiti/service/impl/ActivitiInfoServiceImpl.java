package com.ycs.community.activiti.service.impl;

import com.ycs.community.activiti.dao.ActivitiInfoDao;
import com.ycs.community.activiti.domain.po.FlowMain;
import com.ycs.community.activiti.service.ActivitiInfoService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ActivitiInfoServiceImpl implements ActivitiInfoService {
    @Autowired
    private ActivitiInfoDao activitiInfoDao;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;


    @Override
    public String resolve(Long taskId, Map<String, Object> variables) {
        FlowMain flowMain = new FlowMain();
        flowMain.setTaskId(taskId);
        flowMain.setFlowDefCode(null);
        flowMain.setFlowState(1);
        flowMain.setCreTm(new Date().getTime());
        long id = activitiInfoDao.addFlowMain(flowMain);
        flowMain.setId(id);
        // 运行流程
        String flowId = this.runFlow(flowMain, variables);
        return flowId;
    }

    @Override
    public String runFlow(FlowMain flowMain, Map<String, Object> variables) {
        String flowId = "";
        try {
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowMain.getFlowDefCode(), String.valueOf(flowMain.getId()), variables);
            flowId = processInstance.getProcessInstanceId();
            flowMain.setFlowId(Long.valueOf(flowId));
            flowMain.setUpdTm(new Date().getTime());
            activitiInfoDao.updFlowMain(flowMain);
        } catch (Exception e) {
            throw new CustomizeBusinessException(HiMsgCdConstants.RUN_FLOW_FAIL, "运行流程失败");
        }
        return flowId;
    }

    @Override
    public FlowMain qryFlowMainByTaskId(Long taskId) {
        return activitiInfoDao.qryFlowMainByTaskId(taskId);
    }

    @Override
    public Task qryTaskByInstId(String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        return task;
    }
}
