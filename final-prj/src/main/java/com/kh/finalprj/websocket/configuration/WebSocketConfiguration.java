package com.kh.finalprj.websocket.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer{
	
	//WebSocket 연결 주소 설정
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("*")
				.withSockJS();
	}
	
	//STOMP 메세지 송수신 주소 설정
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//클라이언트 -> 서버
		registry.setApplicationDestinationPrefixes("/app");
		//서버 -> 클라이언트
		registry.enableSimpleBroker("/public", "/private");
	}
	
	//WebSocket 메세지에서도 SpringSecurity 인증정보 사용
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new SecurityContextChannelInterceptor());
	}
	
	//@AuthenticationPrincipal 사용 가능하도록 설정
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
	}
	
	
	
	
	
	
	
	
}
