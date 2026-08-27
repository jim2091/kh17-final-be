package com.kh.finalprj.configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.Cookie;

@Configuration
public class SecurityConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			BearerTokenResolver bearerTokenResolver,
			JwtAuthenticationConverter jwtAuthenticationConverter
	) throws Exception{
		http
			.csrf(csrf -> csrf.disable())
			.cors(Customizer.withDefaults())
			.sessionManagement(
				session -> session.sessionCreationPolicy(
					SessionCreationPolicy.STATELESS
				)
			)
			//기본 로그인화면과 인증 시스템 비활성화
			.formLogin(form->form.disable())
			.httpBasic(basic->basic.disable())
			.logout(logout->logout.disable())
			
			.authorizeHttpRequests(
				auth -> auth
					//무조건 허용
					.requestMatchers(
						"/active"
						,"/swagger-ui/**"
						,"/v3/api-docs/**"
					).permitAll()
					
					.requestMatchers(
							"/service/auth/login"
							,"/service/auth/logout"
							,"/service/auth/refresh"
					).permitAll()
					//admin기능 
					.requestMatchers(
							"/api/admin/add"
					).hasAuthority("admin")
					//일단 다되게(나중에 꼭 바꿔야함)
//					.anyRequest().permitAll()
					//나머지 매핑들은 최소한 로그인은 해야함. 
					.anyRequest().authenticated()
			)
			//JWT 검증 설정
			.oauth2ResourceServer(
				ouath2 -> ouath2
					.bearerTokenResolver(bearerTokenResolver)
					.jwt(
						jwt -> jwt.jwtAuthenticationConverter(
								jwtAuthenticationConverter
						)
					)
			)
			
			//예외 상황 처리 설정
			.exceptionHandling(
				exception -> exception
					.authenticationEntryPoint(
						(req, res, exp) -> res.setStatus(401)
					)
					.accessDeniedHandler(
						(req, res, exp) -> res.setStatus(403)
					)
			)
		;
		
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		
		//접근 허용 대상 지정
		config.setAllowedOrigins(List.of(
			"http://localhost:5173"
		));
		//허용 HTTP 메소드 설정
		config.setAllowedMethods(List.of(
			"GET", "POST", "PUT", "PATCH", "DELETE",
			"OPTIONS",
			"HEAD"
		));
		//허용 HTTP 헤더 설정
		config.setAllowedHeaders(List.of("*"));
		//인증 쿠키 사용 설정
		config.setAllowCredentials(true);
		//preflight 시간 설정
		config.setMaxAge(Duration.ofHours(1L));
		
		//만든 설정을 적용시킬 주소에 등록하는 설정 객체
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(
			"/**", 
			config
		);
		
		return source;
	}
	
	@Bean
	public BearerTokenResolver bearerTokenResolver() {
		return request -> {
	
			//accessToken이 만료되어도 상관없는 주소면 통과
			Set<String> allowPaths = Set.of(
					"/service/auth/login",
					"/service/auth/logout",
					"/service/auth/refresh"
					);
			
			if(allowPaths.contains(request.getServletPath())) {
				return null;
			}
			
			//accessToken을 찾아서 반환
			Cookie[] cookies = request.getCookies();
			
//			for(Cookie cookie : cookies) {
//				
//				System.out.println("name = " + cookie.getName());
//			    System.out.println("value = " + cookie.getValue());
//			}
			if(cookies == null) {
				return null;
			}
			
			
			
			return Arrays.stream(cookies)
					.filter(cookie -> cookie.getName().equals("accessToken"))
					.map(cookie -> cookie.getValue())
					.filter(value -> value != null && !value.isBlank())
					.findFirst()
					.orElse(null);		
		};
		
		
		
	}
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		
		JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
		converter.setAuthoritiesClaimName("authorities");
		converter.setAuthorityPrefix("");
		JwtAuthenticationConverter result = new JwtAuthenticationConverter();
		result.setJwtGrantedAuthoritiesConverter(converter);
		
		return result;
	}
}
