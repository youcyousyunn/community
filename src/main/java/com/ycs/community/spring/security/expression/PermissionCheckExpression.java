package com.ycs.community.spring.security.expression;

import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.sysbo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service(value = "el")
public class PermissionCheckExpression {
    @Autowired
    private AuthService authService;

    /**
     * 验证接口权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        boolean result = authService.hasPermission(String.valueOf(permission));
        if (!result) {
            throw new BadRequestException(HttpStatus.FORBIDDEN, "接口权限不足");
        }
        return true;
    }
}