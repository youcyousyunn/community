package com.ycs.community.spring.context;

import lombok.Data;

@Data
public class BaseRequestInfo {
    private String remoteIp;
    private long requestTm;
    private String url;
    private String urlWithOutContext;
    private String userAgent;
    private long accountId;
}
