package com.ycs.community.spring.config;

import com.ycs.community.spring.property.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PropertyBeanConfig
 * @description: 配置转换成Properties配置类
 * @author: youcyousyunn
 * @create: 2021/03/26
 */
@Configuration
public class PropertyBeanConfig {

    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public SecurityProperties SecurityProperties() {
        return new SecurityProperties();
    }
}
