package com.ycs.community.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycs.community.activiti.command.HistoryProcessInstanceDiagramCommand;
import com.ycs.community.activiti.domain.dto.ActivitiModelRequestDto;
import com.ycs.community.activiti.domain.dto.ActivitiModelResponseDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiModelPageResponseDto;
import com.ycs.community.activiti.service.ActivitiModelService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@Api(tags = "流程管理: 流程管理")
public class ActivitiModelController {
    @Autowired
    private ActivitiModelService activitiModelService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
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
    public ActivitiModelResponseDto delModel(@PathVariable("id") String id) {
        ActivitiModelResponseDto responseDto = new ActivitiModelResponseDto();
        repositoryService.deleteModel(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 部署流程
     * @param request
     * @return
     * @throws IOException
     */
    @PutMapping("/model/deploy")
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

}
