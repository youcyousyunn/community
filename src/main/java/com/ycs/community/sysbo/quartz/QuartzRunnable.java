package com.ycs.community.sysbo.quartz;

import com.ycs.community.spring.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class QuartzRunnable implements Runnable {
    private Logger logger = LoggerFactory.getLogger(QuartzRunnable.class);
    private Object target;
    private Method method;
    private String params;

    /**
     * 构造函数
     * @param beanName
     * @param methodName
     * @param params
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public QuartzRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
        this.target = SpringContextHolder.getBean(beanName);
        this.params = params;
        if (params != null) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(method);
            if (params != null) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            logger.error("定时任务执行失败", e);
        }
    }
}
