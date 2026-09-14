package com.kh.finalprj.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data @Component @ConfigurationProperties(prefix = "custom.kakao")
public class KakaoProperties {
	
	private String clientId;
	private String clientSecret;
	private String redirectUri;

}
