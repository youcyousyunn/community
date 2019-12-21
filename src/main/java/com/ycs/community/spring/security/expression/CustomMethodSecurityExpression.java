package com.ycs.community.spring.security.expression;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * 有点问题，暂时用PermissionCheckExpression代替
 */
public class CustomMethodSecurityExpression extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    public CustomMethodSecurityExpression(Authentication authentication) {
        super(authentication);
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