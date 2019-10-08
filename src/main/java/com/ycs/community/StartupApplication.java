package com.ycs.community;

import com.ycs.community.basebo.constants.Constants;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartupApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(StartupApplication.class);
		application.setBannerMode(Banner.Mode.CONSOLE);
		application.run(args);
	}

}
