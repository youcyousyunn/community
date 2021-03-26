package com.ycs.community.spring.property;

import lombok.Data;

/**
 * community
 *
 * @description: Jwt配置类
 * @author: youcyousyunn
 * @create: 2021/03/26
 */
@Data
public class SecurityProperties {
    private String header;
    private String secret;
    private long expiration;
    private String onlineKey;
    private long detect;
    private long renew;
}
