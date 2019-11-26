package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.JobRequestDto;
import com.ycs.community.sysbo.domain.dto.QryJobPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryJobPageResponseDto;
import com.ycs.community.sysbo.domain.po.JobPo;

import java.util.List;

public interface JobService {
    QryJobPageResponseDto qryJobPage(QryJobPageRequestDto request);
    boolean addJob(JobRequestDto request);
    boolean delJob(Long id);
    boolean updJob(JobRequestDto request);
    List<JobPo> qryJobsByDeptId(Long deptId);
}
