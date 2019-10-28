package com.ycs.community.sysbo.quartz.task;

import org.springframework.stereotype.Component;

@Component
public class TestTask {
    public void run () {
        System.out.println("无参测试任务执行成功！");
    }

    public void run1 (String param) {
        System.out.println("有参测试任务执行成功！" + param);
    }
}
