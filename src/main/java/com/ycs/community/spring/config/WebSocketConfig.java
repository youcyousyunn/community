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

    /**
     * 首先要注入ServerEndpointExporter,这个bean会自动注册使用@ServerEndpoint注解声明的Websocket endpoint
     * 注意: 如果使用独立的servlet容器就不要注入ServerEndpointExporter,因为它将由容器自己提供和管理
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public SpringSocketConfigurator springSocketConfigurator() {
        return new SpringSocketConfigurator();
    }
}