package com.ycs.community.basebo.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRequestDto implements Serializable {
    protected String url;
    protected String urlWithOutContext;
    protected String userAgent;
    protected String remoteIp;
    protected Long requestTm;
}
