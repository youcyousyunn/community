package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QryLogPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryLogPageResponseDto;
import com.ycs.community.sysbo.domain.po.LogJnlPo;

public interface LogService {
    boolean addLog(LogJnlPo logJnlPo);
    QryLogPageResponseDto qryLogPage(QryLogPageRequestDto request);
}
