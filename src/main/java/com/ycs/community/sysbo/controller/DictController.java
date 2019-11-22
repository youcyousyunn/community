package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.*;
import com.ycs.community.sysbo.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 分页查询字典列表
     * @param request
     * @return
     */
    @GetMapping("/dict/queryPage")
    public QryDictPageResponseDto qryDictPage(QryDictPageRequestDto request) {
        QryDictPageResponseDto responsePageDto = new QryDictPageResponseDto();
        responsePageDto = dictService.qryDictPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 分页查询字典详情列表
     * @param request
     * @return
     */
    @GetMapping("/dict/detail/queryPage")
    public QryDictDetailPageResponseDto qryDictDetailPage(QryDictDetailPageRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        QryDictDetailPageResponseDto responsePageDto = new QryDictDetailPageResponseDto();
        responsePageDto = dictService.qryDictDetailPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 根据字典名称查询字典详情
     * @param request
     * @return
     */
    @GetMapping("/dict/detail")
    public DictDetailResponseDto qryDictDetails(DictRequestDto request) {
        DictDetailResponseDto responseDto = new DictDetailResponseDto();
        responseDto = dictService.qryDictDetailsByName(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 添加字典
     * @param request
     * @return
     */
    @PostMapping("/dict")
    public DictResponseDto addDict(@RequestBody DictRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DictResponseDto responseDto = new DictResponseDto();
        dictService.addDict(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 根据id删除字典
     * @param id
     * @return
     */
    @DeleteMapping("/dict/{id}")
    public DictResponseDto delDict(@PathVariable("id") Long id) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(String.valueOf(id))) {
            BizLogger.info("接口请求报文异常" + String.valueOf(id));
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DictResponseDto responseDto = new DictResponseDto();
        dictService.delDict(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新字典
     * @param request
     * @return
     */
    @PutMapping("/dict")
    public DictResponseDto updDict(@RequestBody DictRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DictResponseDto responseDto = new DictResponseDto();
        dictService.updDict(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 添加字典详情
     * @param request
     * @return
     */
    @PostMapping("/dict/detail")
    public DictDetailResponseDto addDictDetail(@RequestBody DictDetailRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DictDetailResponseDto responseDto = new DictDetailResponseDto();
        dictService.addDictDetail(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 根据id删除字典详情
     * @param id
     * @return
     */
    @DeleteMapping("/dict/detail/{id}")
    public DictDetailResponseDto delDictDetail(@PathVariable("id") Long id) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(String.valueOf(id))) {
            BizLogger.info("接口请求报文异常" + String.valueOf(id));
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DictDetailResponseDto responseDto = new DictDetailResponseDto();
        dictService.delDictDetail(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新字典详情
     * @param request
     * @return
     */
    @PutMapping("/dict/detail")
    public DictDetailResponseDto updDictDetail(@RequestBody DictDetailRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DictDetailResponseDto responseDto = new DictDetailResponseDto();
        dictService.updDictDetail(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}