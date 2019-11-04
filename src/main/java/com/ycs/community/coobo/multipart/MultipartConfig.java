package com.ycs.community.coobo.multipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.MultipartConfigElement;
import java.io.File;


@Configuration
public class MultipartConfig {
    @Value("${file.path}")
    private String uploadPath;

    /**
     * 设置上传文件临时目录
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File tmpFile = new File(uploadPath);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation(uploadPath);
        return factory.createMultipartConfig();
    }
}