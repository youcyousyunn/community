package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.sysbo.domain.dto.QryQuartzJobPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryQuartzJobPageResponseDto;
import com.ycs.community.sysbo.domain.dto.QuartzJobRequestDto;
import com.ycs.community.sysbo.domain.dto.QuartzJobResponseDto;
import com.ycs.community.sysbo.service.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuartzJobController {
    @Autowired
    private QuartzJobService quartzJobService;

    /**
     * 分页查询定时任务列表
     * @param request
     * @return
     */
    @GetMapping("/quartz/job/queryPage")
    @OperationLog(title = "分页查询定时任务列表", action = OperationType.GET, isSave = false, channel = "web")
    public QryQuartzJobPageResponseDto qryQuartzPage(QryQuartzJobPageRequestDto request) {
        QryQuartzJobPageResponseDto responseDto = new QryQuartzJobPageResponseDto();
        responseDto = quartzJobService.qryQuartzPage(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 新增定时任务
     * @param request
     * @return
     */
    @PostMapping("/quartz/job")
    @OperationLog(title = "新增定时任务", action = OperationType.POST, isSave = true, channel = "web")
    public QuartzJobResponseDto addJob(QuartzJobRequestDto request) {
        QuartzJobResponseDto responseDto = new QuartzJobResponseDto();
        boolean result = quartzJobService.addJob(request);
        if (result) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 执行定时任务
     * @param id
     * @return
     */
    @PutMapping("/quartz/job/execute/{id}")
    @OperationLog(title = "执行定时任务", action = OperationType.PUT, isSave = true, channel = "web")
    public QuartzJobResponseDto executeJob(@PathVariable("id") Long id) {
        QuartzJobResponseDto responseDto = new QuartzJobResponseDto();
        boolean result = quartzJobService.executeJob(id);
        if (result) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 更新定时任务状态
     * @param id
     * @return
     */
    @PutMapping("/quartz/job/status/{id}")
    @OperationLog(title = "更新定时任务状态", action = OperationType.PUT, isSave = true, channel = "web")
    public QuartzJobResponseDto updJobStatus(@PathVariable("id") Long id) {
        QuartzJobResponseDto responseDto = new QuartzJobResponseDto();
        boolean result = quartzJobService.updJobStatus(quartzJobService.qryJobById(id));
        if (result) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }
}
