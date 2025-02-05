package com.myweb.MusicLD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableFeignClients // Kích hoạt Feign Clients
@EnableJpaRepositories(basePackages = "com.myweb.MusicLD.repository.jpa")
public class MusicLdApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicLdApplication.class, args);
	}
}
