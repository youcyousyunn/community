package com.ycs.community.activiti.controller;

import com.ycs.community.activiti.domain.dto.QryActivitiFlowRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiFlowResponseDto;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageResponseDto;
import com.ycs.community.activiti.service.ActivitiFlowService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiFlowController {
    @Autowired
    private ActivitiFlowService activitiFlowService;


    /**
     * 查询流程列表
     * @param request
     * @return
     */
    @GetMapping("/flow/list")
    public QryActivitiFlowResponseDto queryFlowList(QryActivitiFlowRequestDto request) {
        QryActivitiFlowResponseDto responseDto = new QryActivitiFlowResponseDto();
        responseDto = activitiFlowService.qryFlowList(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
