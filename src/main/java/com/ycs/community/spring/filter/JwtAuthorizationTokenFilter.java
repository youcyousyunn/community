package com.ycs.community.spring.filter;

import com.alibaba.fastjson.JSONObject;
import com.ycs.community.spring.property.SecurityProperties;
import com.ycs.community.spring.security.domain.po.OnlineUserPo;
import com.ycs.community.spring.security.utils.JwtTokenUtil;
import com.ycs.community.sysbo.domain.po.UserPo;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(JwtAuthorizationTokenFilter.class);
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
            IOException {
        // 获取请求令牌
        String authToken = jwtTokenUtil.getToken(request);
        OnlineUserPo onlineUser = null;
        try {
            String jsonStr = (String) redisTemplate.opsForValue().get(securityProperties.getOnlineKey() + authToken);
            onlineUser = JSONObject.parseObject(jsonStr, OnlineUserPo.class);
        } catch (ExpiredJwtException e) {
            logger.error("从缓存获取在线用户信息: {}", e.getMessage());
        }
        if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 此步骤（从数据库中获取用户信息）非必须操作
            UserPo userPo = (UserPo) userDetailsService.loadUserByUsername(onlineUser.getName());
            // 验证令牌是否过期
            if (jwtTokenUtil.validateToken(authToken, userPo)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPo, null, userPo.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 令牌验证通过，给Security授权
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 检查令牌是否续期
                jwtTokenUtil.checkRenewal(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}