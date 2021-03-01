package com.ycs.community.activiti.controller;

import com.ycs.community.activiti.command.HistoryProcessInstanceDiagramCommand;
import com.ycs.community.activiti.domain.dto.ActivitiModelRequestDto;
import com.ycs.community.activiti.domain.dto.ActivitiModelResponseDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageResponseDto;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.activiti.service.ActivitiModelService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "流程管理: 流程管理")
public class ActivitiModelController {
    @Autowired
    private ActivitiModelService activitiModelService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ActivitiFlowService activitiFlowService;
    @Autowired
    private ManagementService managementService;


    /**
     * 分页查询流程列表
     * @param request
     * @return
     */
    @GetMapping("/model/queryPage")
    public QryActivitiModelPageResponseDto queryModelPage(QryActivitiModelPageRequestDto request) {
        QryActivitiModelPageResponseDto responseDto = new QryActivitiModelPageResponseDto();
        responseDto = activitiModelService.qryActivitiModelPage(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 查询流程图
     * @param request
     * @throws IOException
     */
    @GetMapping("/model/queryFlowImg")
    public ActivitiModelResponseDto queryFlowImg(ActivitiModelRequestDto request) throws IOException {
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        String flowId = request.getFlowId();
        // 接口请求报文检查
        if (StringUtils.isEmpty(flowId)) {
            responseDto.setRspCode(HiMsgCdConstants.FLOW_IMG_NOT_EXIST);
            responseDto.setRspInf("流程图不存在");
            return responseDto;
        }
        Command<InputStream> cmd = new HistoryProcessInstanceDiagramCommand(flowId);
        InputStream inputStream = managementService.executeCommand(cmd);

        if (null != inputStream) {
            byte[] data = IOUtils.toByteArray(inputStream);
            String img = Base64.getEncoder().encodeToString(data);
            responseDto.setImg(img);
        }
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除流程
     * @param id
     * @return
     */
    @DeleteMapping("/model/{id}")
    public ActivitiModelResponseDto delModel(@PathVariable("id") String id, @RequestParam("key") String key) {
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        repositoryService.deleteModel(id);
        activitiFlowService.delFlowDefByKey(key);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 部署流程
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/model/deploy")
    public ActivitiModelResponseDto deployModel(@RequestBody ActivitiModelRequestDto request) throws IOException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        activitiModelService.deployModel(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 复制流程
     * @param request
     * @return
     */
    @PostMapping("/model/copy")
    public ActivitiModelResponseDto copyModel(@RequestBody ActivitiModelRequestDto request) {
        Model modelData = repositoryService.newModel();
        Model oldModel = repositoryService.getModel(request.getId());
        modelData.setName(oldModel.getName() + " — 复制");
        modelData.setKey(oldModel.getKey());
        modelData.setMetaInfo(oldModel.getMetaInfo());
        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), this.repositoryService.getModelEditorSource(oldModel.getId()));
        repositoryService.addModelEditorSourceExtra(modelData.getId(), this.repositoryService.getModelEditorSourceExtra(oldModel.getId()));
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 挂起流程
     * @param request
     * @return
     */
    @PostMapping("/model/suspend")
    public ActivitiModelResponseDto suspendModel(@RequestBody ActivitiModelRequestDto request) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(request.getKey())) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        if (StringUtils.isEmpty(request.getDeploymentId())) {
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "该流程尚未部署");
        }
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(request.getKey())
                .list();
        processDefinitions.forEach(processDefinition -> {
            boolean suspended = processDefinition.isSuspended();
            if(!suspended) {
                repositoryService.suspendProcessDefinitionById(processDefinition.getId(), true, new Date());
            }
        });
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 激活流程
     * @param request
     * @return
     */
    @PostMapping("/model/activate")
    public ActivitiModelResponseDto activateModel(@RequestBody ActivitiModelRequestDto request) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(request.getKey())) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        if (StringUtils.isEmpty(request.getDeploymentId())) {
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "该流程尚未部署");
        }
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(request.getKey())
                .list();
        processDefinitions.forEach(processDefinition -> {
            boolean suspended = processDefinition.isSuspended();
            if(suspended) {
                repositoryService.activateProcessDefinitionById(processDefinition.getId(), true, new Date());
            }
        });
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
