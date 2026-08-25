package com.kh.finalprj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//(exclude = {
//		SecurityAutoConfiguration.class,
//		OAuth2ResourceServerAutoConfiguration.class
//})
public class FinalPrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalPrjApplication.class, args);
	}

}
