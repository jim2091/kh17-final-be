package com.kh.finalprj.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kh.finalprj.configuration.KakaoProperties;
import com.kh.finalprj.vo.kakao.KakaoTokenResponseVO;
import com.kh.finalprj.vo.kakao.KakaoUserInfoResponseVO;

@Service
public class KakaoService {
	
	@Qualifier("kakaoAuthClient")
	@Autowired
	private WebClient kakaoAuthClient;
	
	@Qualifier("kakaoApiClient")
	@Autowired
	private WebClient kakaoApiClient;
	
	@Autowired
	private KakaoProperties kakaoProperties;
	
    public String kakaoLogin() throws Exception {

        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoProperties.getClientId()
                + "&redirect_uri=" + URLEncoder.encode(
                        kakaoProperties.getRedirectUri(),
                        StandardCharsets.UTF_8
                  )
                + "&response_type=code";

        return url;
    }
    
    public KakaoTokenResponseVO getKakaoToken(String code) {
    	return kakaoAuthClient.post()
    			.uri("/oauth/token")
    			.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
    			.bodyValue(
    					"grant_type=authorization_code"
    					+ "&client_id="+ kakaoProperties.getClientId()
    					+ "&client_secret=" + kakaoProperties.getClientSecret()
    					+ "&redirect_uri=" + URLEncoder.encode(
    							kakaoProperties.getRedirectUri(),
    							StandardCharsets.UTF_8
    							)
    					+ "&code=" + URLEncoder.encode(
    							code,
    							StandardCharsets.UTF_8
    							)
    					)
    			.retrieve()
    			.bodyToMono(KakaoTokenResponseVO.class)
    			.block();
    			
    }
    
    public KakaoUserInfoResponseVO getKakaoUserInfo(String accessToken) {
    	return kakaoApiClient.get()
    			.uri("/v2/user/me")
    			.header("Authorization", "Bearer "+ accessToken)
    			.retrieve()
    			.bodyToMono(KakaoUserInfoResponseVO.class)
    			.block();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
