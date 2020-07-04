package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.QryRedisPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRedisPageResponseDto;
import com.ycs.community.sysbo.domain.dto.RedisRequestDto;

public interface RedisService {
    QryRedisPageResponseDto qryRedisPage(QryRedisPageRequestDto request);
    boolean delRedis(RedisRequestDto request);
    boolean clearRedis();
    boolean addVerifyCode(String uuid, String code);
    String qryVCode(String key);
    boolean delVCode(String key);
    String get(String key);
    boolean addTmpTicket(String key, String value, int expire);
    String qryTmpTicket(String key);
    boolean delTmpTicket(String key);
}
