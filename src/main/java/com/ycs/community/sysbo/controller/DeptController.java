package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.DeptRequestDto;
import com.ycs.community.sysbo.domain.dto.DeptResponseDto;
import com.ycs.community.sysbo.domain.po.DataScope;
import com.ycs.community.sysbo.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private DataScope dataScope;

    /**
     * 查询部门树
     * @param request
     * @return
     */
    @GetMapping("/dept/tree")
    public DeptResponseDto qryDeptTree(DeptRequestDto request) {
        DeptResponseDto responseDto = new DeptResponseDto();
        // 设置用户查看部门权限
        request.setIds(dataScope.getDeptIds());
        responseDto = deptService.qryDeptTree(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 添加部门
     * @param request
     * @return
     */
    @PostMapping("/dept")
    public DeptResponseDto addDept(@RequestBody DeptRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DeptResponseDto responseDto = new DeptResponseDto();
        deptService.addDept(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @DeleteMapping("/dept/{id}")
    public DeptResponseDto delDept(@PathVariable("id") Long id) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(id)) {
            BizLogger.info("接口请求报文异常" + id);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        DeptResponseDto responseDto = new DeptResponseDto();
        deptService.delDept(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 更新部门
     * @param request
     * @return
     */
    @PutMapping("/dept")
    public DeptResponseDto updDept(@RequestBody DeptRequestDto request) {
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