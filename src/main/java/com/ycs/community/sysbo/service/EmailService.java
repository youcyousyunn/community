package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.EmailRequestDto;
import com.ycs.community.sysbo.domain.dto.EmailResponseDto;
import com.ycs.community.sysbo.domain.po.EmailPo;

import java.util.List;

public interface EmailService {
    boolean sendEmail(EmailRequestDto request);
    EmailResponseDto qryEmailConfig();
    boolean updEmailConfig(EmailRequestDto request);
    List<EmailPo> qryEmailReceivers(String filter);
}
