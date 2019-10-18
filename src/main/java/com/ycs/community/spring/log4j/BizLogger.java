package com.ycs.community.spring.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BizLogger {
    private static Logger logger = LoggerFactory.getLogger(BizLogger.class);

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void info(String msg, Throwable e) {
        logger.info(msg, e);
    }
}
