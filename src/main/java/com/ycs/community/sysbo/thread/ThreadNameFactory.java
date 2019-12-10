package com.ycs.community.sysbo.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ThreadNameFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String threadName;

    public ThreadNameFactory() {
        this("el-pool");
    }

    private ThreadNameFactory(String prefix) {
        SecurityManager securityManager = System.getSecurityManager();
        group = (null != securityManager) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        // 线程名= prefix + 第几个用这个工厂创建线程池的
        this.threadName = prefix +  poolNumber.getAndIncrement();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        // 线程名= threadName + -thread- + 这个线程池中第几个执行的线程
        Thread thread = new Thread(group, runnable,threadName + "-thread-" + threadNumber.getAndIncrement(),0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}