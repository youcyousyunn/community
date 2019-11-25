package com.ycs.community.sysbo.utils;

import org.apache.poi.util.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // 使用AnnotationConfigApplicationContext加载@Configuration注解的spring容器
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        TestBean testBean1 = (TestBean) applicationContext.getBean("testBean");
        testBean1.say();
        System.out.println(testBean1);
        System.out.println("--------------------------------");

        TestBean testBean2 = (TestBean) applicationContext.getBean("testBean");
        testBean2.say();
        System.out.println(testBean2);
    }
}
