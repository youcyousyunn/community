package com.ycs.community.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.ycs.community.spring.annotation.CmmOperationLog)")
    public void operationLog(){
    }

    /**
     * 环绕通知
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long startTime = System.currentTimeMillis();
        try {
            res =  joinPoint.proceed();
            return res;
        } finally {
            try {
                //方法执行完成后增加日志
                long endTime = System.currentTimeMillis();
//                addOperationLog(joinPoint,res,time);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        System.out.println("方法执行前执行");
    }

    /**
     * 后置通知
     * @param joinPoint
     */
    @After("operationLog()")
    public void after(JoinPoint joinPoint){
        System.out.println("方法执行后执行");
    }

    /**
     * 处理完请求，返回内容
     * @param result
     */
    @AfterReturning(returning = "result", pointcut = "operationLog()")
    public void doAfterReturning(Object result) {
        logger.info("请求响应： {}", result); // 加入花括号{} 解决第二个参数不能打印
    }

    /**
     * 异常通知
     * @param joinPoint
     */
    @AfterThrowing("operationLog()")
    public void throwing(JoinPoint joinPoint){
    }
}
