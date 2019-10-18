package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.sysbo.domain.dto.QryRedisPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRedisPageResponseDto;
import com.ycs.community.sysbo.domain.dto.RedisRequestDto;
import com.ycs.community.sysbo.domain.dto.RedisResponseDto;
import com.ycs.community.sysbo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisService redisService;

    /**
     * 分页查询Redis缓存列表
     * @param request
     * @return
     */
    @GetMapping("/redis")
    public QryRedisPageResponseDto qryRedisPage(QryRedisPageRequestDto request) {
        QryRedisPageResponseDto responsePageDto = new QryRedisPageResponseDto();
        redisService.qryRedisPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 根据key删除单个Redis缓存
     * @param request
     * @return
     */
    @DeleteMapping("/redis")
    public RedisResponseDto delRedis(RedisRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        RedisResponseDto responseDto = new RedisResponseDto();
        if (redisService.delRedis(request)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 清空Redis缓存
     * @return
     */
    @DeleteMapping("/redis/all")
    public RedisResponseDto clearRedis() {
        RedisResponseDto responseDto = new RedisResponseDto();
        if(redisService.clearRedis()) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return  responseDto;
    }
}
