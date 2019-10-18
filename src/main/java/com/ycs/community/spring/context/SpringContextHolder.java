package com.ycs.community.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextHolder.applicationContext != null) {
            logger.warn("SpringContextHolder中的ApplicationContext被覆盖");
        }
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取存储在静态变量中的ApplicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected(); // 断言
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 断言 ApplicationContext不为空
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext属性未注入, 请在SpringBoot启动类中注册SpringContextHolder");
        }
    }

    /**
     * 清除 SpringContextHolder中的ApplicationContext为Null
     */
    public static void clearHolder() {
        logger.debug("清除SpringContextHolder中的ApplicationContext{}" + applicationContext);
        applicationContext = null;
    }

    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }
}
