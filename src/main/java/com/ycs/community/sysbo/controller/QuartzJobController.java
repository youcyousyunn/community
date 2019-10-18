package com.ycs.community.sysbo.controller;

import com.ycs.community.sysbo.domain.dto.QuartzJobRequestDto;
import com.ycs.community.sysbo.domain.dto.QuartzJobResponseDto;
import com.ycs.community.sysbo.service.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuartzJobController {
    @Autowired
    private QuartzJobService quartzJobService;

    /**
     * 新增定时任务
     * @param request
     * @return
     */
    @PostMapping("/job")
    public QuartzJobResponseDto addJob(QuartzJobRequestDto request) {
        QuartzJobResponseDto responseDto = new QuartzJobResponseDto();
        quartzJobService.addJob(request);
        return responseDto;
    }
}
