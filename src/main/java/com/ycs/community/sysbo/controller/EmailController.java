package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.EmailRequestDto;
import com.ycs.community.sysbo.domain.dto.EmailResponseDto;
import com.ycs.community.sysbo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    /**
     * 更新邮件配置
     * @param request
     * @return
     */
    @PutMapping("/email")
    public EmailResponseDto updEmailConfig(@RequestBody EmailRequestDto request) {
        // 接口请求报文检查
        if (!request.checkMailConfigRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        EmailResponseDto responseDto = new EmailResponseDto();
        if (emailService.updEmailConfig(request)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_EMAIL_CONFIG_FAIL, "更新邮件配置失败");
        }
        return responseDto;
    }

    /**
     * 查询邮件配置
     * @return
     */
    @GetMapping("/email")
    public EmailResponseDto qryEmailConfig() {
        EmailResponseDto responseDto = new EmailResponseDto();
        responseDto = emailService.qryEmailConfig();
        if (!StringUtils.isEmpty(responseDto)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 发送邮件
     * @param request
     * @return
     */
    @PostMapping("/email")
    public EmailResponseDto sendEmail(@RequestBody EmailRequestDto request) {
        // 接口请求报文检查
        if (!request.checkSendMailRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        EmailResponseDto responseDto = new EmailResponseDto();
        emailService.sendEmail(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}