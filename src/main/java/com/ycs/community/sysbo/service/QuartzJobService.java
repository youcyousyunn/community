package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QuartzJobRequestDto;
import com.ycs.community.sysbo.domain.po.QuartzJobPo;

public interface QuartzJobService {
    boolean addJob(QuartzJobRequestDto request);
    boolean updJobIsPause(Long id, Boolean isPause);
}
