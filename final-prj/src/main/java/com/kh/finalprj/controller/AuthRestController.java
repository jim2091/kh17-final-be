package com.kh.finalprj.controller;

import java.time.Duration;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.configuration.JwtProperties;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dao.EmpRefreshDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.EmpRefreshDto;
import com.kh.finalprj.service.AuthService;
import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.vo.auth.AuthLoginRequestVO;
import com.kh.finalprj.vo.auth.AuthLoginResponseVO;
import com.kh.finalprj.vo.jwt.TokenCreateRequestVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="인증 처리 서비스", description="stateless 서버의 인증 처리 로직 구현")
@CommonsApiResponse

@RestController
@RequestMapping("/service/auth")
public class AuthRestController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private JwtProperties jwtProperties;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private EmpRefreshDao empRefreshDao;
	
	@ApiResponse(responseCode="200", description="로그인성공")
	@ApiResponse(responseCode="404", description="로그인 정보 불일치")
	@PostMapping(value="/login", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthLoginResponseVO> login(
			@RequestHeader(
					value = "User-Agent",
					required = false,
					defaultValue = "UNKNOWN"
			) String userAgent,
			HttpServletRequest req,
			@RequestBody AuthLoginRequestVO request
			) {
		//로그인 처리를 수행하고 결과를 얻어낸다
		AuthLoginResponseVO response = authService.login(request);
		
		
		//차단된 회원이면 403처리
		EmpDto empDto = empDao.selectOne(request.getEmpEmail());
		
		//토큰 생성
		TokenCreateRequestVO tokenRequest = new TokenCreateRequestVO();
		BeanUtils.copyProperties(response, tokenRequest);
		
		String accessToken = jwtService.createAccessToken(tokenRequest);
		String refreshToken = jwtService.createRefreshToken(tokenRequest.getEmpNo());
		
		
		//쿠키생성
		ResponseCookie accessCookie = ResponseCookie
				.from("accessToken", accessToken)
				//각종설정들(만료시간,
				.maxAge(Duration.ofSeconds(
						jwtProperties.getAccessTokenValidity()
				))//유효시간30분
				.path("/")//적용범위
				.httpOnly(true)//
				.secure(false)//https 사용 여부
				.sameSite("Lax")//허용범위(NONE:자유, Lax:유연, Strict:엄격)
				.build();
		ResponseCookie refreshCookie = ResponseCookie
				.from("refreshToken", refreshToken)
				//각종설정들(만료시간,
				.maxAge(Duration.ofSeconds(
						jwtProperties.getRefreshTokenValidity()
				))//유효시간4주
				.path("/service/auth/")//적용범위
				.httpOnly(true)//
				.secure(false)//https 사용 여부
				.sameSite("Lax")//허용범위(NONE:자유, Lax:유연, Strict:엄격)
				.build();
		
		//refresh token 정보를 DB에 저장
		empRefreshDao.insertOrUpdate(
				EmpRefreshDto.builder()
					.empNo(response.getEmpNo())
					.userAgent(userAgent)
					.userAddress(req.getRemoteAddr())
					.tokenValue(refreshToken)
				.build()
		);
		
		
		
		
		//결과 반환
		return ResponseEntity.ok()
				//쿠키를 추가하는 설정
					.header(
							HttpHeaders.SET_COOKIE, 
							accessCookie.toString(),
							refreshCookie.toString()
							)
					.body(response);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
