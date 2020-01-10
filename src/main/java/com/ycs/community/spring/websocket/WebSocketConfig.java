package com.ycs.community.spring.websocket;

import com.ycs.community.spring.handler.IWebSocketHandler;
import com.ycs.community.spring.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;

@Component
@EnableWebMvc
//@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Resource
    private IWebSocketHandler webSocketHandler;
    @Value("${host.host-name}")
    private String hostName;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.webSocketHandler, new String[]{"/socket.io"})
                .setAllowedOrigins(hostName)
                .addInterceptors(new HandshakeInterceptor[]{new WebSocketInterceptor()});
//		registry.addHandler(this.webSocketHandler, new String[]{"/ws/ws.do"})
//				.addInterceptors(new HandshakeInterceptor[]{new WebSocketInterceptor()}).withSockJS();
    }
}