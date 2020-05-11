package com.ycs.community.spring.config;

import com.ycs.community.spring.interceptor.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.path}")
    private String path;

    @Autowired
    private CommonInterceptor commonInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("POST", "DELETE", "PUT", "GET");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathUrl = Paths.get(path).normalize().toUri().toASCIIString();
        registry.addResourceHandler("/attach/**").addResourceLocations(pathUrl).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0); // 配置web访问swagger-ui.html
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**");
    }
}
