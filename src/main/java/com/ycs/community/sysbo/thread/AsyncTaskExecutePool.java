package com.ycs.community.sysbo.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncTaskExecutePool implements AsyncConfigurer {
    private Logger logger = LoggerFactory.getLogger(AsyncTaskExecutePool.class);
    // 注入配置类
    private final AsyncTaskProperties asyncTaskProperties;

    public AsyncTaskExecutePool(AsyncTaskProperties asyncTaskProperties) {
        this.asyncTaskProperties = asyncTaskProperties;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(asyncTaskProperties.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(asyncTaskProperties.getMaxPoolSize());
        // 队列容量
        executor.setQueueCapacity(asyncTaskProperties.getQueueCapacity());
        // 活跃时间
        executor.setKeepAliveSeconds(asyncTaskProperties.getKeepAliveSeconds());
        // 线程名字前缀
        executor.setThreadNamePrefix("el-async-");
        // setRejectedExecutionHandler: 当pool已经达到max-size的时候，如何处理新任务
        // CallerRunsPolicy: 不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            logger.error("执行线程异常消息{}", throwable);
            logger.error("执行方法名{}", method.getName());
        };
    }
}