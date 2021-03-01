package com.ycs.community.activiti.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ycs.community.activiti.domain.dto.ActivitiModelRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageResponseDto;
import com.ycs.community.activiti.domain.po.FlowDef;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.activiti.service.ActivitiModelService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ActivitiModelServiceImpl implements ActivitiModelService {
    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private ActivitiFlowService activitiFlowService;


    @Override
    public QryActivitiModelPageResponseDto qryActivitiModelPage(QryActivitiModelPageRequestDto request) {
        int currentPage = request.getCurrentPage();
        int pageSize = request.getPageSize();
        long totalCount = repositoryService.createModelQuery().count();
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if(!StringUtils.isEmpty(request.getName())) {
            modelQuery.modelNameLike("%"+request.getName()+"%");
        }
        List<Model> data = modelQuery.orderByCreateTime().desc().listPage((currentPage-1)*pageSize, pageSize);
        // 设置流程key
        data.forEach(model -> {
            if(!StringUtils.isEmpty(model.getDeploymentId())) {
                FlowDef flowDef = activitiFlowService.qryFlowDefByDeploymentId(model.getDeploymentId());
                model.setKey(flowDef != null ? flowDef.getKey() : "");
            }
        });
        // 组装分页信息
        QryActivitiModelPageResponseDto response = new QryActivitiModelPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal((int) totalCount);
            return response;
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean deployModel(ActivitiModelRequestDto request) throws IOException {
        String modelId = request.getId();
        if (!StringUtils.isEmpty(modelId)) {
            Model model = repositoryService.getModel(modelId);
            if(StringUtils.isEmpty(model)) {
                throw new CustomizeBusinessException(HiMsgCdConstants.FLOW_NOT_EXIST, "流程已不存在");
            }
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
            byte[] bpmnBytes = null;

            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
            String processName = model.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(model.getName()).addString(processName, new String(bpmnBytes,"utf-8")).deploy();
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);

            // 往流程定义表中添加流程数据
            FlowDef flowDef = new FlowDef();
            List<Process> processes = bpmnModel.getProcesses();
            for (Process process : processes) {
                flowDef.setKey(process.getId());
                flowDef.setName(process.getName());
            }
            flowDef.setState(1);
            activitiFlowService.addFlowDef(flowDef);
        }
        return true;
    }
}
