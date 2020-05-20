package com.ycs.community.spring.annotation;

import com.ycs.community.spring.enums.LimitTypeEnum;

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
    LimitTypeEnum limitType() default LimitTypeEnum.CUSTOMER;
}