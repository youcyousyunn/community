package com.ycs.community.spring.security.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserPo implements Serializable {
    private static final long serialVersionUID = 3065891170336873304L;
    private String key;
    private String name;
    private String job;
    private String avatar;
    private String requestIp;
    private String address;
    private String browser;
    private Date loginTm;
}