package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QryQuartzJobPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryQuartzJobPageResponseDto;
import com.ycs.community.sysbo.domain.dto.QuartzJobRequestDto;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;

import java.util.List;

public interface QuartzJobService {
    boolean addJob(QuartzJobRequestDto request);
    QryQuartzJobPageResponseDto qryQuartzPage(QryQuartzJobPageRequestDto request);
    boolean executeJob(Long id);
    List<QuartzJobPo> qryJobByRunning();
    QuartzJobPo qryJobById(Long id);
    boolean updJobStatus(QuartzJobPo quartzJobPo);
}
