package com.ycs.community.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LimitFlowAspect {

    @Pointcut("@annotation()")
    public void pointcut() {
    }


}