package com.zerobase.timate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@PropertySource("classpath:env.properties")  // env.properties 파일을 로드
public class TimateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimateApplication.class, args);
	}

}
