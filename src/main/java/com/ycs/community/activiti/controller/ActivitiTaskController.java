package com.ycs.community.activiti.controller;

import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageRequestDto;
import com.ycs.community.activiti.domain.dto.QryActivitiTaskPageResponseDto;
import com.ycs.community.activiti.service.ActivitiTaskService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiTaskController {
    @Autowired
    private ActivitiTaskService activitiTaskService;


    /**
     * 分页查询我的任务列表
     * @param request
     * @return
     */
    @GetMapping("/task/queryPage")
    public QryActivitiTaskPageResponseDto queryMyTask(QryActivitiTaskPageRequestDto request) {
        QryActivitiTaskPageResponseDto responseDto = new QryActivitiTaskPageResponseDto();
        responseDto = activitiTaskService.queryMyTaskPage(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

}
