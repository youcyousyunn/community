package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.EmailRequestDto;
import com.ycs.community.sysbo.domain.dto.EmailResponseDto;

public interface EmailService {
    boolean sendEmail(EmailRequestDto request);
    EmailResponseDto qryEmailConfig();
    boolean updEmailConfig(EmailRequestDto request);
}
