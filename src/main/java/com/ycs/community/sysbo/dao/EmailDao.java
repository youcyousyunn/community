package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.EmailPo;
import com.ycs.community.sysbo.email.EmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmailDao {
    EmailConfig qryEmailConfig();
    int updEmailConfig(EmailConfig emailConfig);
    int addEmailConfig(EmailConfig emailConfig);
    List<EmailPo> qryEmailReceivers(@Param("filter") String filter);
}
