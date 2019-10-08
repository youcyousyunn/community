package com.ycs.community.spring.interceptor;

import com.ycs.community.basebo.constants.Constants;
import com.ycs.community.spring.context.BaseRequestInfo;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Component
public class CommonInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        this.initRequestInfo(request);
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:80");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 @Nullable Exception ex) throws Exception {
    }

    /**
     * 初始化请求
     * @param request
     * @return
     */
    private BaseRequestInfo initRequestInfo (HttpServletRequest request) {
        String reqUrl = request.getRequestURI();
        String reqUrlWithoutCtx = reqUrl.substring(reqUrl.indexOf(request.getContextPath()));
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute(Constants.AUTH_TOKEN);
        System.out.println(token);
        Enumeration<String> enumeration = request.getHeaders(Constants.AUTH_TOKEN);
        String authToken = null;
        long accountId = 0;
        while (enumeration.hasMoreElements()) {
            authToken = enumeration.nextElement();
        }
        if (!StringUtils.isEmpty(authToken)) {
            accountId = (long) session.getAttribute(authToken);
        }
        request.setAttribute("accountId", accountId);
        BaseRequestInfo requestInfo = new BaseRequestInfo();
//        requestInfo.setRemoteIp(this.getClientIP(request));
//        requestInfo.setRequestTm(System.currentTimeMillis());
//        requestInfo.setUrl(reqUrl);
//        requestInfo.setUrlWithOutContext(reqUrlWithoutCtx);
//        requestInfo.setUserAgent(this.getUserAgent(request));
//        requestInfo.setAccountId(accountId);
        return requestInfo;
    }

    /**
     * 获取客户端IP
     * @param request
     * @return
     */
    private String getClientIP(HttpServletRequest request) {
        String clientIP = request.getHeader("X-Real-IP");
        if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("Proxy-Client-IP");
        }

        if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("x-forwarded-for");
            if (!StringUtils.isEmpty(clientIP) && !"unKnown".equalsIgnoreCase(clientIP)) {
                // 多次反向代理后会有多个IP值，第一个IP才是真实IP
                int index = clientIP.indexOf(",");
                if (index != -1){
                    clientIP = clientIP.substring(0, index);
                }
            }
        }

        if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("WL-Proxy-Client-IP");
        }

        if (clientIP == null || clientIP.length() == 0 || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getRemoteAddr();
        }

        return clientIP;
    }

    /**
     * 获取用户代理
     * @param request
     * @return
     */
    private String getUserAgent (HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
