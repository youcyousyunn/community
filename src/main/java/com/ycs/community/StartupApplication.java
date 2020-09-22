package com.ycs.community;

import com.ycs.community.spring.context.SpringContextHolder;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StartupApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(StartupApplication.class);
		application.setBannerMode(Banner.Mode.CONSOLE);
		application.run(args);
	}

	/**
	 * 在SpringBoot启动类中注册SpringContextHolder
	 * @return
	 */
	@Bean
	@Scope("singleton") // spring中默认注册bean均为单例
	public SpringContextHolder springContextHolder () {
		return new SpringContextHolder();
	}
}
