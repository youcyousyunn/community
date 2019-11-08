package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

@Data
public class LogJnlPo extends BasePo {
    private Long id;
    private String userNm;
    private String method;
    private String params;
    private String description;
    private String type;
    private Long costTime;
    private String requestIp;
    private String address;
    private String browser;
}
