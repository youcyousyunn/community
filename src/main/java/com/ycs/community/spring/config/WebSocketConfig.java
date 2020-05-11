package com.ycs.community.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启socket支持
 */
@Configuration
//@ConditionalOnWebApplication
public class WebSocketConfig {
//    @Autowired
//    private WebSocketInterceptor webSocketInterceptor;
//    @Autowired
//    private WebSocketHandler webSocketHandler;

    /**
     * 首先要注入ServerEndpointExporter,这个bean会自动注册使用@ServerEndpoint注解声明的Websocket endpoint
     * 注意: 如果使用独立的servlet容器就不要注入ServerEndpointExporter,因为它将由容器自己提供和管理
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

//    @Bean
//    public MySpringConfigurator mySpringConfigurator() {
//        return new MySpringConfigurator();
//    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        //部分 支持websocket 的访问链接,允许跨域
//        registry.addHandler(webSocketHandler, "/socket/*").addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
//        //部分 不支持websocket的访问链接,允许跨域
//        registry.addHandler(webSocketHandler, "/socket/*").addInterceptors(webSocketInterceptor).setAllowedOrigins("*").withSockJS();
//    }
}