package com.kh.finalprj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//스케쥴러를 이용
@EnableScheduling
//(exclude = {
//		SecurityAutoConfiguration.class,
//		OAuth2ResourceServerAutoConfiguration.class
//})
public class FinalPrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalPrjApplication.class, args);
	}

}
