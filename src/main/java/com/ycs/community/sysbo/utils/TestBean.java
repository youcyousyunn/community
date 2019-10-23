package com.ycs.community.sysbo.utils;

import org.springframework.stereotype.Component;

public class TestBean {
    private String name;

    public void say() {
        System.out.println("你的名字是: "+name);
    }

    public void init() {
        System.out.println("初始化init方法执行...");
    }

    public void destroy() {
        System.out.println("销毁destroy方法执行...");
    }
}
