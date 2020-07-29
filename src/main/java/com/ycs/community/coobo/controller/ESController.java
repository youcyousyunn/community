package com.ycs.community.coobo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;
import com.ycs.community.coobo.service.ESService;
import com.ycs.community.spring.exception.CustomizeRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PutMapping("/es")
    public ESPageResponseDto qryContentPage(@RequestBody ESPageRequestDto request) throws IOException {
        ESPageResponseDto responseDto = new ESPageResponseDto();
        if (!StringUtils.isEmpty(esService.qryContentPage(request))) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }
}
