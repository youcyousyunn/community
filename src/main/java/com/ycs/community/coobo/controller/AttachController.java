package com.ycs.community.coobo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.coobo.domain.dto.AttachRequestDto;
import com.ycs.community.coobo.domain.dto.AttachResponseDto;
import com.ycs.community.coobo.domain.dto.QryAttachPageRequestDto;
import com.ycs.community.coobo.domain.dto.QryAttachPageResponseDto;
import com.ycs.community.coobo.service.AttachService;
import com.ycs.community.spring.exception.CustomizeRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AttachController {
    @Autowired
    private AttachService attachService;

    /**
     * 分页查询附件列表
     * @param request
     * @return
     */
    @GetMapping("/attach/queryPage")
    public QryAttachPageResponseDto qryAttachPage(QryAttachPageRequestDto request) {
        QryAttachPageResponseDto responsePageDto = new QryAttachPageResponseDto();
        responsePageDto = attachService.qryAttachPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 上传附件
     * @param file
     * @param name
     * @return
     */
    @PostMapping("/attach")
    public AttachResponseDto upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        AttachResponseDto responseDto = new AttachResponseDto();
        attachService.upload(file, name);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @DeleteMapping("/attach/{id}")
    public AttachResponseDto delAttach(@PathVariable("id") Long id) {
        AttachResponseDto responseDto = new AttachResponseDto();
        if(attachService.delAttach(id)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 删除多个附件
     * @param ids
     * @return
     */
    @DeleteMapping("/attach")
    public AttachResponseDto delAllAttach(@RequestBody Long[] ids) {
        AttachResponseDto responseDto = new AttachResponseDto();
        if(attachService.delAllAttach(ids)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 更新附件
     * @param request
     * @return
     */
    @PutMapping("/attach")
    public AttachResponseDto updAttach(@RequestBody AttachRequestDto request) {
        if (!request.checkRequestDto()) {
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        AttachResponseDto responseDto = new AttachResponseDto();
        if (attachService.updAttach(request)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }

    /**
     * 导出附件信息到Excel
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/attach/download")
    public AttachResponseDto downAttach(AttachRequestDto request, HttpServletResponse response) {
        AttachResponseDto responseDto = new AttachResponseDto();
        if (attachService.downAttach(request, response)) {
            responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        }
        return responseDto;
    }
}
