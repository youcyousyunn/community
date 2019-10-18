package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.sysbo.domain.dto.QryRedisPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryRedisPageResponseDto;
import com.ycs.community.sysbo.domain.dto.RedisRequestDto;
import com.ycs.community.sysbo.domain.po.RedisPo;
import com.ycs.community.sysbo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${login.vCode.expiration}")
    private long loginVCodeExpiration;

    @Override
    public QryRedisPageResponseDto qryRedisPage(QryRedisPageRequestDto request) {
        List<RedisPo> data = new ArrayList<>();
        for (Object key : redisTemplate.keys(request.getKey())) {
            String k = String.valueOf(key);
            RedisPo redisPo = new RedisPo(k, redisTemplate.opsForValue().get(k).toString());
            data.add(redisPo);
        }
        // 查询总条数
        int totalCount = data.size();
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 组装分页信息
        QryRedisPageResponseDto response = new QryRedisPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    public boolean delRedis(RedisRequestDto request) {
        boolean result = redisTemplate.delete(request.getKey());
        return result;
    }

    @Override
    public boolean clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        return true;
    }

    @Override
    public boolean addVerifyCode(String uuid, String code) {
        redisTemplate.opsForValue().set(uuid, code);
        redisTemplate.expire(uuid, loginVCodeExpiration, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public String qryVCode(String key) {
        String value = redisTemplate.opsForValue().get(key).toString();
        return value;
    }
}