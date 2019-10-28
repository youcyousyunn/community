package com.ycs.community.sysbo.quartz;

import com.ycs.community.spring.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class QuartzRunnable implements Callable {
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
        if (!StringUtils.isEmpty(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public Object call() throws Exception {
        ReflectionUtils.makeAccessible(method);
        if (!StringUtils.isEmpty(params)) {
            method.invoke(target, params);
        } else {
            method.invoke(target);
        }
        return null;
    }
}
