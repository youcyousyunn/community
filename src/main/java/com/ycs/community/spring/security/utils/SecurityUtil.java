package com.ycs.community.spring.security.utils;

import cn.hutool.json.JSONObject;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.spring.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    /**
     * 获取登录用户信息
     * @return
     */
    public static UserDetails getUserPo() {
        UserPo userPo;
        try {
            userPo = (UserPo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }

        return userPo;
    }

    /**
     * 获取用户名
     * @return
     */
    public static String getUserName() {
        Object obj = getUserPo();
        return new JSONObject(obj).get("name", String.class);
    }

    /**
     * 获取用户Id
     * @return
     */
    public static Long getUserId() {
        Object obj = getUserPo();
        return new JSONObject(obj).get("id", Long.class);
    }
}
