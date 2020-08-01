package com.ycs.community.coobo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;
import com.ycs.community.coobo.service.ESService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ESController {
    @Autowired
    private ESService esService;

    /**
     * 分页查询文档列表
     * @param request
     * @return
     */
    @GetMapping("/es/queryPage")
    @AnonymousAccess
    public ESPageResponseDto qryContentPage(ESPageRequestDto request) throws IOException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "请输入查询关键字");
        }
        ESPageResponseDto responseDto = new ESPageResponseDto();
        responseDto = esService.qryContentPage(request);
        if (!StringUtils.isEmpty(responseDto)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }
}
