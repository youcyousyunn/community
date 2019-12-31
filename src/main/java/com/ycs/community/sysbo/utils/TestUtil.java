package com.ycs.community.sysbo.utils;

import org.springframework.util.StringUtils;

public class TestUtil {
    public static void main(String[] args) {
        Long id = null;
        if (StringUtils.isEmpty(id)) {
            System.out.println("null");
        }
        System.out.println(String.valueOf(id));
    }
}