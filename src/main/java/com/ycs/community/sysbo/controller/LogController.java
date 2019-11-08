package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.sysbo.domain.dto.QryLogPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryLogPageResponseDto;
import com.ycs.community.sysbo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}