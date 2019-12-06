package com.ycs.community.sysbo.utils;

import org.apache.poi.util.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Integer> levels = new ArrayList<>();
        levels.add(1);
        levels.add(2);
        levels.add(3);
        System.out.println(Collections.min(levels));

        String str = "D:/study/Vue/Projects/eladmin/upload/attach";
        String[] array = str.split("/");
        System.out.println(array.toString());

        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1);
        list.add(2);
        List<Integer> newList = list.stream().distinct().collect(Collectors.toList());
        System.out.println(newList);
    }
}
