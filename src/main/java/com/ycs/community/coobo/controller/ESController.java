package com.ycs.community.coobo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;
import com.ycs.community.coobo.domain.dto.ESRequestDto;
import com.ycs.community.coobo.domain.dto.ESResponseDto;
import com.ycs.community.coobo.service.ESService;
import com.ycs.community.spring.annotation.AnonymousAccess;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Api(tags = "系统工具: 全文检索")
public class ESController {
    @Autowired
    private ESService esService;

    /**
     * 分页查询文档列表
     * @param request
     * @return
     */
    @GetMapping("/es/queryPage")
    public ESPageResponseDto qryContentPage(ESPageRequestDto request) throws IOException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "请输入查询关键字");
        }
        ESPageResponseDto pageResponseDto = new ESPageResponseDto();
        pageResponseDto = esService.qryContentPage(request);
        if (!StringUtils.isEmpty(pageResponseDto)) {
            pageResponseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return pageResponseDto;
    }

    /**
     * 新增文档
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/es")
    public ESResponseDto addDoc(@RequestBody ESRequestDto request) throws IOException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ESResponseDto responseDto = new ESResponseDto();
        if (esService.addDoc(request)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 删除文档
     * @param id
     * @return
     * @throws IOException
     */
    @DeleteMapping("/es/{id}")
    public ESResponseDto delDocById(@PathVariable("id") String id) throws IOException {
        // 接口请求报文检查
        if (id == "" || id == null) {
            BizLogger.info("接口请求报文异常" + id);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ESResponseDto responseDto = new ESResponseDto();
        if (esService.delDocById(id)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 更新文档
     * @param request
     * @return
     * @throws IOException
     */
    @PutMapping("/es")
    public ESResponseDto updDoc(@RequestBody ESRequestDto request) throws IOException {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        ESResponseDto responseDto = new ESResponseDto();
        if (esService.updDoc(request)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }
}
