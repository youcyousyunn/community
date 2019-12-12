package com.ycs.community.sysbo.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.EmailDao;
import com.ycs.community.sysbo.domain.dto.EmailRequestDto;
import com.ycs.community.sysbo.domain.dto.EmailResponseDto;
import com.ycs.community.sysbo.domain.po.EmailPo;
import com.ycs.community.sysbo.email.EmailConfig;
import com.ycs.community.sysbo.service.EmailService;
import com.ycs.community.sysbo.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailDao emailDao;

    @Override
    public EmailResponseDto qryEmailConfig() {
        EmailConfig emailConfig = emailDao.qryEmailConfig();
        if (StringUtils.isEmpty(emailConfig)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.EMAIL_CONFIG_NOT_EXIST, "邮件配置不存在");
        }
        EmailResponseDto response = new EmailResponseDto();
        response.setData(emailConfig);
        return response;
    }

    @Override
    public boolean updEmailConfig(EmailRequestDto request) {
        int result = -1;
        EmailConfig oldEmailConfig = emailDao.qryEmailConfig();
        try {
            // 对称加密
            request.setPassword(EncryptUtil.desEncrypt(request.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        EmailConfig emailConfig = BeanUtil.trans2Entity(request, EmailConfig.class);
        if (!StringUtils.isEmpty(oldEmailConfig)) {
            emailConfig.setUpdTm(new Date().getTime());
            result = emailDao.updEmailConfig(emailConfig);
        } else {
            emailConfig.setCreTm(new Date().getTime());
            result = emailDao.addEmailConfig(emailConfig);
        }
        if (result == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<EmailPo> qryEmailReceivers(String filter) {
        return emailDao.qryEmailReceivers(filter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendEmail(EmailRequestDto request) {
        EmailConfig emailConfig = emailDao.qryEmailConfig();
        if (StringUtils.isEmpty(emailConfig)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.EMAIL_CONFIG_NOT_EXIST, "邮件配置不存在");
        }
        // 封装发送邮件实体
        MailAccount account = new MailAccount();
        account.setHost(emailConfig.getHost());
        account.setPort(emailConfig.getPort());
        account.setAuth(true);
        try {
            account.setPass(EncryptUtil.desDecrypt(emailConfig.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(emailConfig.getUser() + "<"+emailConfig.getFromUser()+">");
        account.setSslEnable(true);

        // 发送邮件
        try {
            Mail.create(account)
                    .setTos(request.getReceiver())
                    .setTitle(request.getSubject())
                    .setContent(request.getContent())
                    .setHtml(true)
                    // 关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

        return true;
    }
}
