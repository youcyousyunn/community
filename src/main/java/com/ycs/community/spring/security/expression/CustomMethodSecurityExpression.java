package com.ycs.community.spring.security.expression;

import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.sysbo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpression extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
//    @Autowired
//    private AuthService authService;

    public CustomMethodSecurityExpression(Authentication authentication) {
        super(authentication);
//        this.authService = authService;
    }

    /**
     * 验证接口权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
//        boolean result = authService.hasPermission(permission);
        if (!false) {
            throw new BadRequestException("接口权限不足");
        }
        return true;
    }

    @Override
    public void setFilterObject(Object o) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object o) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }
}