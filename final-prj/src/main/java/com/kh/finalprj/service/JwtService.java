package com.kh.finalprj.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.kh.finalprj.configuration.JwtProperties;
import com.kh.finalprj.vo.jwt.TokenCreateRequestVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

@Service
public class JwtService {
	
	@Autowired
	private JwtEncoder jwtEncoder;
	@Autowired
	private JwsHeader jwsHeader;
	@Autowired
	private JwtProperties jwtProperties;
	@Autowired
	private JwtDecoder jwtDecoder;
	
	public String createAccessToken(TokenCreateRequestVO request) {
		
		Instant current = Instant.now();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
					.issuer(jwtProperties.getIssuer())
					.issuedAt(current)
					.expiresAt(current.plusSeconds(jwtProperties.getAccessTokenValidity()))
					.subject(String.valueOf(request.getEmpNo()))
					.claim("empNo", request.getEmpNo())
					.claim("empLevel", request.getEmpLevel())
					.claim("authorities", List.of(
							request.getEmpLevel()
							))
				.build();
		
		return jwtEncoder
					.encode(JwtEncoderParameters.from(jwsHeader, claims))
					.getTokenValue();
	}
	
	public TokenParseResponseVO parseAccessToken(String token) {
		Jwt jwt = jwtDecoder.decode(token);
		
		int empNo = Integer.parseInt(jwt.getSubject());
		
//		System.out.println("empNo : "+ empNo);
		
		return TokenParseResponseVO.builder()
					.empNo(empNo)
					.empLevel(jwt.getClaimAsString("empLevel"))
				.build();
	}
	
	public String createRefreshToken(int empNo) {
		Instant current = Instant.now();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
					.issuer(jwtProperties.getIssuer())
					.issuedAt(current)
					.expiresAt(current.plusSeconds(jwtProperties.getRefreshTokenValidity()))
					.subject(String.valueOf(empNo))
				.build();
		
		return jwtEncoder
				.encode(JwtEncoderParameters.from(jwsHeader, claims))
				.getTokenValue();
	}
	
	public String parseRefreshToken(String token) {
		Jwt jwt = jwtDecoder.decode(token);
		return jwt.getSubject();
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
