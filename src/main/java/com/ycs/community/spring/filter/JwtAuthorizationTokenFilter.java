package com.ycs.community.spring.filter;

import com.alibaba.fastjson.JSONObject;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.spring.security.domain.po.OnlineUserPo;
import com.ycs.community.spring.security.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jwt.online.key}")
    private String onlineKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
            IOException {
        String authToken = jwtTokenUtil.getToken(request);
        OnlineUserPo onlineUser = null;
        try {
            String jsonStr = (String) redisTemplate.opsForValue().get(onlineKey + authToken);
            onlineUser = JSONObject.parseObject(jsonStr, OnlineUserPo.class);
        } catch (ExpiredJwtException e) {
            logger.error("从缓存获取在线用户信息: {}", e.getMessage());
        }
        if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            UserPo userPo = (UserPo) userDetailsService.loadUserByUsername(onlineUser.getName());
            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, userPo)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPo, null, userPo.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}