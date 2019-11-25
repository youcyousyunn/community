package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;
import com.ycs.community.sysbo.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 查询部门树
     * @param request
     * @return
     */
    @GetMapping("/dept/tree")
    public DeptResponseDto qryDeptTree(DeptRequestDto request) {
        DeptResponseDto responseDto = new DeptResponseDto();
        responseDto = deptService.qryDeptTree(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除部门
     * @param request
     * @return
     */
    @DeleteMapping("/dept")
    public DeptResponseDto delDept(DeptRequestDto request) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(request.getId())) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DeptResponseDto responseDto = new DeptResponseDto();
        deptService.delDept(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新部门
     * @param request
     * @return
     */
    @PutMapping("/dept")
    public DeptResponseDto updDept(DeptRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DeptResponseDto responseDto = new DeptResponseDto();
        deptService.updDept(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }
}