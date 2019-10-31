package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.email.EmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmailDao {
    EmailConfig qryEmailConfig();
    int updEmailConfig(EmailConfig emailConfig);
    int addEmailConfig(EmailConfig emailConfig);
}
