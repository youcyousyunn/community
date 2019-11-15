package com.ycs.community.spring.security.utils;

import cn.hutool.json.JSONObject;
import com.ycs.community.spring.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    /**
     * 获取用户信息
     * @return
     */
    public static UserDetails getUserDetails() {
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }

        return userDetails;
    }

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername() {
        Object obj = getUserDetails();
        return new JSONObject(obj).get("username", String.class);
    }

    /**
     * 获取用户Id
     * @return
     */
    public static Long getUserId() {
        Object obj = getUserDetails();
        return new JSONObject(obj).get("id", Long.class);
    }
}
