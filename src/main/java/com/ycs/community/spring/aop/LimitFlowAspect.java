package com.ycs.community.spring.aop;

import com.google.common.collect.ImmutableList;
import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.spring.annotation.LimitFlow;
import com.ycs.community.spring.enums.LimitType;
import com.ycs.community.spring.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LimitFlowAspect {
    private Logger logger = LoggerFactory.getLogger(LimitFlowAspect.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${limit.flow.expiration}")
    private Long expiration;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.ycs.community.spring.annotation.LimitFlow)")
    public void pointcut() {
    }

    /**
     * 环绕通知
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        LimitFlow limitFlow = signatureMethod.getAnnotation(LimitFlow.class);
        LimitType limitType = limitFlow.limitType();
        String key = limitFlow.key();
        if (StringUtils.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = getIP(request);
            } else {
                key = signatureMethod.getName();
            }
        }

        ImmutableList<Object> keys = ImmutableList.of(StringUtils.join(limitFlow.prefix(), "_", key, "_", request.getRequestURI().replaceAll("/","_")));

        String limitFlowScript = buildLimitFlowScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(limitFlowScript, Number.class);
        Number count = (Number) redisTemplate.execute(redisScript, keys, limitFlow.count(), limitFlow.period());
        // 判断当前访问是否已限流
        String token = request.getHeader(Constants.AUTH_TOKEN);
        Object isLimited = redisTemplate.opsForValue().get(key + token);
        if (null != isLimited && "true".equals(String.valueOf(isLimited))) {
            throw new BadRequestException("访问太频繁, 已限流");
        } else {
            if (null != count && count.intValue() <= limitFlow.count()) {
                logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limitFlow.name());
                return joinPoint.proceed();
            } else {
                redisTemplate.opsForValue().set(key + token, "true");
                redisTemplate.expire(key + token, expiration, TimeUnit.MINUTES);
                throw new BadRequestException("访问太频繁, 开始限流");
            }
        }
    }

    /**
     * 获取IP
     * @param request
     * @return
     */
    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if  ("127.0.0.1".equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 限流脚本
     * @return
     */
    private String buildLimitFlowScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}