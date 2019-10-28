package com.ycs.community.sysbo.utils;

import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetTest {
    public static void main(String[] args) {
        Set<Person> set = new HashSet<>();
        set.add(new Person("张三", 20));
        set.add(new Person("李四", 21));
        set.add(new Person("王二", 22));

        boolean isAdded = set.add(new Person("张三", 20));
        System.out.println("添加无hashCode与equals方法对象是否成功: " + isAdded);
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
