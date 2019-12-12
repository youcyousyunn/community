package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EmailPo extends BasePo {
    private String name; // 收件人姓名
    private String receiver; // 多个收件人以英文分号(;)隔开
    private String subject;
    private String content;
}
