package com.ycs.community.spring.annotation;

import com.ycs.community.spring.enums.OperationType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CmmOperationLog {
    String title() default "";
    OperationType action() default OperationType.UNKNOWN;
    boolean isSave() default false;
    String channel() default "";
}
