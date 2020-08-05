package com.ycs.community.coobo.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class IdGeneratorSnowflakeUtil {
    private static long workerId = 0;
    private static long dataCenterId = 1;
    private static Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);

    @PostConstruct // 被@PostConstruct修饰的方法会在构造函数之后执行，并且只会执行一次
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("获取当前机器workerId: {}", workerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("获取当前机器workerId失败", e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }

    }

    /**
     * 默认生成唯一ID
     * @return
     */
    public static synchronized long generateFlakeId() {
        return snowflake.nextId();
    }

    /**
     * 自定义生成唯一ID
     * @param workerId
     * @param dataCenterId
     * @return
     */
    public static synchronized long generateCustomFlakeId(long workerId, long dataCenterId) {
        Snowflake customSnowflake = IdUtil.createSnowflake(workerId, dataCenterId);
        return customSnowflake.nextId();
    }


    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        try {
            for (int i=1; i<=30; i++) {
                threadPool.submit(() -> {
                    System.out.println(IdGeneratorSnowflakeUtil.generateFlakeId());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
