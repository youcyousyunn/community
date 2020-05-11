package com.ycs.community.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;

/**
 * 由于websocket注册的bean默认是自己管理并没有托管给spring,此类的作用是将websocket的其他bean托管给spring容器
 */
public class SpringSocketConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static volatile BeanFactory context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringSocketConfigurator.context = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }
}
