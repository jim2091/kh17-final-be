package com.kh.finalprj.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoConfiguration {
	
	@Autowired
	private KakaoProperties kakaoProperties;
	
	@Bean("kakaoAuthClient")
	public WebClient kakaoAuthClient() {
		return WebClient.builder()
				.baseUrl("https://kauth.kakao.com")
				.build();
	}
	
	@Bean("kakaoApiClient")
	public WebClient kakaoApiClient() {
		return WebClient.builder()
				.baseUrl("https://kapi.kakao.com")
				.build();
	}

}
