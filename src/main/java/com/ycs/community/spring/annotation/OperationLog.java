package com.ycs.community.spring.annotation;

import com.ycs.community.spring.enums.OperationTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String title() default "";
    OperationTypeEnum action() default OperationTypeEnum.UNKNOWN;
    boolean isSave() default false;
    String channel() default "";
}
