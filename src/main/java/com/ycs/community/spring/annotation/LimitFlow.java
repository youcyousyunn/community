package com.ycs.community.spring.annotation;

import com.ycs.community.spring.enums.LimitType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LimitFlow {
    String key() default "";
    String name() default "";
    String prefix() default "LIMIT_FLOW";
    int period();
    int count();
    LimitType limitType() default LimitType.CUSTOMER;
}