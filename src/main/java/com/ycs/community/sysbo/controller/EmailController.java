package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.enums.OperationType;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.EmailRequestDto;
import com.ycs.community.sysbo.domain.dto.EmailResponseDto;
import com.ycs.community.sysbo.domain.po.EmailPo;
import com.ycs.community.sysbo.service.EmailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "系统工具: 邮件工具")
public class EmailController {
    @Autowired
    private EmailService emailService;

    /**
     * 更新邮件配置
     * @param request
     * @return
     */
    @PutMapping("/email")
    @OperationLog(title = "更新邮件配置", action = OperationType.PUT, isSave = true, channel = "web")
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
    @OperationLog(title = "发送邮件", action = OperationType.POST, isSave = false, channel = "web")
    public EmailResponseDto qryEmailConfig() {
        EmailResponseDto responseDto = new EmailResponseDto();
        responseDto = emailService.qryEmailConfig();
        if (!StringUtils.isEmpty(responseDto)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 匹配收件人
     * @param filter
     * @return
     */
    @GetMapping("/email/{filter}")
    @OperationLog(title = "匹配收件人", action = OperationType.POST, isSave = false, channel = "web")
    public ResponseEntity qryEmailReceivers(@PathVariable("filter") String filter) {
        List<EmailPo> data = emailService.qryEmailReceivers(filter);
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * 发送邮件
     * @param request
     * @return
     */
    @PostMapping("/email")
    @OperationLog(title = "发送邮件", action = OperationType.POST, isSave = true, channel = "web")
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
