package com.ycs.community.spring.interceptor;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class CommonInterceptor implements HandlerInterceptor {
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        initRequestInfo(request);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
    }

    /**
     * 初始化请求信息
     * @param request
     * @return
     */
    private BaseRequestDto initRequestInfo (HttpServletRequest request) {
        String reqUrl = request.getRequestURI();
        String reqUrlWithoutCtx = reqUrl.substring(reqUrl.indexOf(request.getContextPath()));
        UserPo userPo = (UserPo) userDetailsService.loadUserByUsername(SecurityUtil.getUserName());
        BaseRequestDto requestInfo = new BaseRequestDto();
        requestInfo.setAccountId(userPo.getAccountId());
        requestInfo.setUrl(reqUrl);
        requestInfo.setUrlWithOutContext(reqUrlWithoutCtx);
        requestInfo.setUserAgent(this.getUserAgent(request));
        requestInfo.setRemoteIp(this.getIP(request));
        requestInfo.setRequestTm(System.currentTimeMillis());
        request.getServletContext();
        return requestInfo;
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
     * 获取用户代理
     * @param request
     * @return
     */
    private String getUserAgent (HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}