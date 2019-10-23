package com.ycs.community.sysbo.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfiguration {
    public TestConfiguration () {
        System.out.println("TestConfiguration容器启动...");
    }

    // @Bean注解注册bean,同时指定初始化和销毁方法
    @Bean(name = "testBean", initMethod = "init", destroyMethod = "destroy")
    @Scope("singleton")
    public TestBean testBean () {
        return new TestBean();
    }
}
