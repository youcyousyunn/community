package com.ycs.community.sysbo.controller;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.spring.exception.CustomizeRequestException;
import com.ycs.community.spring.log4j.BizLogger;
import com.ycs.community.sysbo.domain.dto.JobRequestDto;
import com.ycs.community.sysbo.domain.dto.JobResponseDto;
import com.ycs.community.sysbo.domain.dto.QryJobPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryJobPageResponseDto;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {
    @Autowired
    private JobService jobService;

    /**
     * 分页查询岗位列表
     * @param request
     * @return
     */
    @GetMapping("/job/queryPage")
    public QryJobPageResponseDto qryJobPage(QryJobPageRequestDto request) {
        QryJobPageResponseDto responsePageDto = new QryJobPageResponseDto();
        responsePageDto = jobService.qryJobPage(request);
        responsePageDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responsePageDto;
    }

    /**
     * 添加岗位
     * @param request
     * @return
     */
    @PostMapping("/job")
    public JobResponseDto addJob(@RequestBody JobRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        JobResponseDto responseDto = new JobResponseDto();
        jobService.addJob(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 删除岗位
     * @param id
     * @return
     */
    @DeleteMapping("/job/{id}")
    public JobResponseDto delJob(@PathVariable("id") Long id) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(id)) {
            BizLogger.info("接口请求报文异常" + id);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        JobResponseDto responseDto = new JobResponseDto();
        jobService.delJob(id);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 修改岗位
     * @param request
     * @return
     */
    @PutMapping("/job")
    public JobResponseDto updJob(@RequestBody JobRequestDto request) {
        // 接口请求报文检查
        if (!request.checkRequestDto()) {
            BizLogger.info("接口请求报文异常" + request.toString());
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        JobResponseDto responseDto = new JobResponseDto();
        jobService.updJob(request);
        responseDto.setRspCode(HiMsgCdConstants.SUCCESS);
        return responseDto;
    }

    /**
     * 获取部门下所有岗位
     * @param deptId
     * @return
     */
    @GetMapping("/job/{deptId}")
    public ResponseEntity qryJobsByDeptId(@PathVariable("deptId") Long deptId) {
        // 接口请求报文检查
        if (StringUtils.isEmpty(deptId)) {
            BizLogger.info("接口请求报文异常" + deptId);
            throw new CustomizeRequestException(HiMsgCdConstants.TX_REQUESTBODY_FAIL, "接口请求报文异常");
        }
        List<JobPo> jobs = jobService.qryJobsByDeptId(deptId);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}