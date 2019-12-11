package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.sysbo.domain.dto.LogRequestDto;
import com.ycs.community.sysbo.domain.dto.LogResponseDto;
import com.ycs.community.sysbo.domain.dto.QryLogPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryLogPageResponseDto;
import com.ycs.community.sysbo.service.LogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统监控: 操作日志")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 分页查询日志列表
     * @param request
     * @return
     */
    @GetMapping("/log/queryPage")
    public QryLogPageResponseDto qryLogPage(QryLogPageRequestDto request) {
        QryLogPageResponseDto responsePageDto = new QryLogPageResponseDto();
        responsePageDto = logService.qryLogPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 根据id查询单个日志
     * @param id
     * @return
     */
    @GetMapping("/log/{id}")
    public LogResponseDto qryLog(@PathVariable("id") Long id) {
        LogResponseDto responseDto = new LogResponseDto();
        responseDto = logService.qryLogById(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}