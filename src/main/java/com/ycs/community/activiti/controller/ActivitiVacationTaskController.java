package com.ycs.community.activiti.controller;

import com.ycs.community.activiti.domain.dto.*;
import com.ycs.community.activiti.service.ActivitiProcessLogService;
import com.ycs.community.activiti.service.ActivitiVacationTaskService;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivitiVacationTaskController {
    @Autowired
    private ActivitiVacationTaskService activitiVacationTaskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;


    /**
     * 分页查询我的请假任务列表
     * @param request
     * @return
     */
    @GetMapping("/vacation/task/queryPage")
    public QryActivitiVacationTaskPageResponseDto queryMyTask(QryActivitiVacationTaskPageRequestDto request) {
        QryActivitiVacationTaskPageResponseDto responseDto = new QryActivitiVacationTaskPageResponseDto();
        responseDto = activitiVacationTaskService.qryMyVacationTaskPage(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 分页查询所有请假任务列表
     * @param request
     * @return
     */
    @GetMapping("/vacation/task/all/queryPage")
    public QryActivitiVacationTaskPageResponseDto queryAllTask(QryActivitiVacationTaskPageRequestDto request) {
        QryActivitiVacationTaskPageResponseDto responseDto = new QryActivitiVacationTaskPageResponseDto();
        responseDto = activitiVacationTaskService.queryAllVacationTask(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 查询流程操作日志
     * @param request
     * @return
     */
    @GetMapping("/vacation/task/queryProcessLog")
    public ActivitiProcessLogResponseDto queryProcessLog(ActivitiProcessLogRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ActivitiProcessLogResponseDto responseDto = new ActivitiProcessLogResponseDto();
        responseDto = activitiProcessLogService.qryOperLog(request.getProcessId());
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 请假审批
     * @param request
     * @return
     */
    @PostMapping("/vacation/task/approve")
    public ActivitiVacationTaskResponseDto approveVacationTask(@RequestBody ActivitiVacationTaskRequestDto request) {
        ActivitiVacationTaskResponseDto responseDto = new ActivitiVacationTaskResponseDto();
        activitiVacationTaskService.approveVacationTask(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除请假申请
     * @param id
     * @return
     */
    @DeleteMapping("/vacation/task/{id}")
    public ActivitiVacationTaskResponseDto delVacationTask(@PathVariable(value = "id") Long id) {
        ActivitiVacationTaskResponseDto responseDto = new ActivitiVacationTaskResponseDto();
        activitiVacationTaskService.delVacationTaskById(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 提交请假申请
     * @param request
     * @return
     */
    @PutMapping("/vacation/task/submit")
    public ActivitiVacationTaskResponseDto submitVacationTask(@RequestBody ActivitiVacationTaskRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ActivitiVacationTaskResponseDto responseDto = new ActivitiVacationTaskResponseDto();
        activitiVacationTaskService.submitVacationTask(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新请假申请
     * @param request
     * @return
     */
    @PutMapping("/vacation/task")
    public ActivitiVacationTaskResponseDto updVacationTask(@RequestBody ActivitiVacationTaskRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ActivitiVacationTaskResponseDto responseDto = new ActivitiVacationTaskResponseDto();
        activitiVacationTaskService.updVacationTask(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 新增请假申请
     * @param request
     * @return
     */
    @PostMapping("/vacation/task")
    public ActivitiVacationTaskResponseDto addVacationTask(@RequestBody ActivitiVacationTaskRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ActivitiVacationTaskResponseDto responseDto = new ActivitiVacationTaskResponseDto();
        activitiVacationTaskService.addVacationTask(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}
