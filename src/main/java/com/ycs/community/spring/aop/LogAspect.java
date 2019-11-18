package com.ycs.community.spring.aop;

import cn.hutool.core.io.resource.ClassPathResource;
import com.ycs.community.coobo.utils.FileUtil;
import com.ycs.community.spring.annotation.OperationLog;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.domain.po.LogJnlPo;
import com.ycs.community.sysbo.service.LogService;
import com.ycs.community.sysbo.utils.ThrowableUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Autowired
    private LogService logService;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.ycs.community.spring.annotation.OperationLog)")
    public void pointcut(){
    }

    /**
     * 环绕通知
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        // 开始时间
        long startTime = System.currentTimeMillis();
        result =  joinPoint.proceed();
        // 结束时间
        long endTime = System.currentTimeMillis();
        LogJnlPo logJnlPo = this.buildLogJnlPo(joinPoint);
        logJnlPo.setType("INFO");
        logJnlPo.setCostTime(endTime - startTime);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method reflectMethod = methodSignature.getMethod();
        OperationLog operationLog = reflectMethod.getAnnotation(OperationLog.class);
        if (operationLog.isSave()) {
            logService.addLog(logJnlPo);
        }
        return result;
    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("pointcut()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        System.out.println("方法执行前执行");
    }

    /**
     * 后置通知
     * @param joinPoint
     */
    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint){
        System.out.println("方法执行后执行");
    }

    /**
     * 处理完请求，返回内容
     * @param result
     */
    @AfterReturning(returning = "result", pointcut = "pointcut()")
    public void doAfterReturning(Object result) {
        logger.info("请求响应： {}", result); // 加入花括号{} 解决第二个参数不能打印
    }

    /**
     * 异常通知
     * @param joinPoint
     */
    @AfterThrowing(value = "pointcut()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        LogJnlPo logJnlPo = this.buildLogJnlPo((ProceedingJoinPoint) joinPoint);
        logJnlPo.setException(ThrowableUtil.getStackTrace(throwable));
        logJnlPo.setType("ERROR");
        logJnlPo.setCostTime(System.currentTimeMillis() - startTime);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method reflectMethod = methodSignature.getMethod();
        OperationLog operationLog = reflectMethod.getAnnotation(OperationLog.class);
        if (operationLog.isSave()) {
            logService.addLog(logJnlPo);
        }
    }

    /**
     * 组装日志实体
     * @param joinPoint
     * @return
     */
    private LogJnlPo buildLogJnlPo(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 组装日志对象
        LogJnlPo logJnlPo = new LogJnlPo();
        logJnlPo.setUserNm(SecurityUtil.getUserName());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String method = joinPoint.getTarget().getClass().getName() + "." + methodSignature.getName() + "()";
        logJnlPo.setMethod(method);
        StringBuilder params = new StringBuilder("{");
        // 参数值
        Object[] argValues = joinPoint.getArgs();
        // 参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        logJnlPo.setParams(params.toString() + " }");
        Method reflectMethod = methodSignature.getMethod();
        OperationLog operationLog = reflectMethod.getAnnotation(OperationLog.class);
        logJnlPo.setDescription(operationLog.title());
        logJnlPo.setRequestIp(getIP(request));
        logJnlPo.setAddress(getAddressByIp(getIP(request)));
        logJnlPo.setBrowser(getBrowser(request));
        logJnlPo.setCreTm(new Date().getTime());
        return logJnlPo;
    }

    /**
     * 获取IP
     * @param request
     * @return
     */
    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if  ("127.0.0.1".equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 获取浏览器
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 根据ip获取地址
     * @param ip
     * @return
     */
    public static String getAddressByIp(String ip) {
        try {
            String path = "ip2region/ip2region.db";
            String name = "ip2region.db";
            DbConfig config = new DbConfig();
            File file = FileUtil.inputStreamToFile(new ClassPathResource(path).getStream(), name);
            DbSearcher searcher = new DbSearcher(config, file.getPath());
            Method method;
            method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock;
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String address = dataBlock.getRegion().replace("0|","");
            if(address.charAt(address.length()-1) == '|'){
                address = address.substring(0,address.length() - 1);
            }
            return address.equals("内网IP|内网IP") ? "内网IP" : address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
