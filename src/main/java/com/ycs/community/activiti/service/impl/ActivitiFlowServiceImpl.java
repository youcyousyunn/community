package com.ycs.community.activiti.service.impl;

import com.ycs.community.activiti.dao.ActivitiFlowDao;
import com.ycs.community.activiti.domain.dto.QryActivitiFlowRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiFlowResponseDto;
import com.ycs.community.activiti.domain.po.FlowDef;
import com.ycs.community.activiti.domain.po.FlowMain;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitiFlowServiceImpl implements ActivitiFlowService {
    @Autowired
    private ActivitiFlowDao activitiFlowDao;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;


    @Override
    public QryActivitiFlowResponseDto qryFlowList(QryActivitiFlowRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        if (!StringUtils.isEmpty(request.getName())) {
            paramMap.put("name", request.getName());
        }
        List<FlowDef> data = activitiFlowDao.qryFlowList(paramMap);
        QryActivitiFlowResponseDto response = new QryActivitiFlowResponseDto();
        if(!CollectionUtils.isEmpty(data)) {
            response.setData(data);
        }
        return response;
    }

    @Override
    public boolean addFlowDef(FlowDef flowDef) {
        //判断库中是否已存在
        FlowDef data = activitiFlowDao.qryFlowDefByCode(flowDef.getKey());
        if(StringUtils.isEmpty(data)) {
            flowDef.setCreTm(new Date().getTime());
            if(activitiFlowDao.addFlowDef(flowDef) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.ADD_FLOW_DEF_FAIL, "添加流程定义失败");
            }
        } else {
            flowDef.setUpdTm(new Date().getTime());
            if(activitiFlowDao.updFlowDef(flowDef) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.UPD_FLOW_DEF_FAIL, "更新流程定义失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public String resolve(Long processId, Long flowDefId, Map<String, Object> variables) {
        FlowMain flowMain = new FlowMain();
        flowMain.setProcessId(processId);
        flowMain.setState(1);
        flowMain.setCreTm(new Date().getTime());
        this.activitiFlowDao.addFlowMain(flowMain);
        // 运行流程
        String flowId = this.runFlow(flowDefId, flowMain, variables);
        return flowId;
    }

    /**待验证
     * 注意：在同一个Service内部，事务与事务方法之间的调用，事务与普通方法之间的嵌套调用，都不会开启新的事务（即2个方法共用调用方的事务）。
     * spring 在扫描bean的时候会扫描方法上是否包含@Transactional注解，如果包含 spring会为这个bean动态地生成一个子类（即代理类，proxy），
     * 代理类是继承原来那个bean的。此时，当这个有注解的方法被调用的时候，实际上是由代理类来调用的，代理类在调用之前就会启动transaction。
     * 然而，如果这个有注解的方法是被同一个类中的其他方法（不论是否有事务注解）调用的，那么该方法的调用并没有通过代理类，而是直接通过原来的那个bean，
     * 所以就不会启动transaction，我们看到的现象就是@Transactional注解无效。
     */
    public String runFlow(Long flowDefId, FlowMain flowMain, Map<String, Object> variables) {
        String flowId = "";
        FlowDef flowDef = activitiFlowDao.qryFlowDefById(flowDefId);
        try {
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowDef.getKey(), String.valueOf(flowMain.getId()), variables);
            flowId = processInstance.getProcessInstanceId();
            flowMain.setFlowId(Long.valueOf(flowId));
            flowMain.setUpdTm(new Date().getTime());
            activitiFlowDao.updFlowMain(flowMain);
        } catch (Exception e) {
            throw new CustomizeBusinessException(HiMsgCdConstants.RUN_FLOW_FAIL, e.getMessage());
        }
        return flowId;
    }

    @Override
    public FlowMain qryFlowMainByTaskId(Long taskId) {
        return activitiFlowDao.qryFlowMainByTaskId(taskId);
    }

    @Override
    public Task qryTaskByInstId(String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        return task;
    }

    @Override
    public FlowMain qryFlowMainById(Long id) {
        return activitiFlowDao.qryFlowMainById(id);
    }

    @Override
    public FlowDef qryFlowDefByDeploymentId(String deploymentId) {
        return activitiFlowDao.qryFlowDefByDeploymentId(deploymentId);
    }

    @Override
    public boolean delFlowDefByKey(String key) {
        if(activitiFlowDao.delFlowDefByKey(key) < 1) {
            return false;
        }
        return true;
    }
}
