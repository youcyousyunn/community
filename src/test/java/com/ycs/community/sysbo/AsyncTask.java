package com.ycs.community.sysbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {
    protected final Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Async
    public void doTask1(int i) throws InterruptedException {
        logger.info("任务 " + i + " 执行");
    }
}