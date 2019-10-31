package com.ycs.community.sysbo.email;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class EmailConfig extends BasePo {
    private Long id;
    private String host; // 邮件服务器SMTP地址
    private Integer port; // 邮件服务器SMTP端口
    private String user; // 发件人用户名, 默认为发件人邮箱前缀
    private String fromUser; // 发件人
    private String password;
}
