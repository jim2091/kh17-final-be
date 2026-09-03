package com.kh.finalprj.controller;

import java.time.Duration;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.kh.finalprj.error.WhoAreYouException;
import com.kh.finalprj.service.AuthService;
import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.vo.auth.AuthLoginRequestVO;
import com.kh.finalprj.vo.auth.AuthLoginResponseVO;
import com.kh.finalprj.vo.jwt.TokenCreateRequestVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

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
		
//		System.out.println("refreshToken : "+ refreshToken);
		
		
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
	
	
	@DeleteMapping("/logout")
	public ResponseEntity<Void> logout(
			@RequestHeader(
					value="User-Agent",
					required = false,
					defaultValue = "UNKNOWN"
			)String userAgent,
			HttpServletRequest req,
			
			@CookieValue(value = "accessToken", required=false) String accessToken,
			@CookieValue(value = "refreshToken", required=false) String refreshToken
			){
		
		ResponseCookie accessCookie = ResponseCookie
					.from("accessToken", "")
					.maxAge(Duration.ZERO)
					.path("/")
					.httpOnly(true)
					.secure(false)
					.sameSite("Lax")
					.build();
		
		ResponseCookie refreshCookie = ResponseCookie
				.from("refreshToken", "")
				.maxAge(Duration.ZERO)
				.path("/service/auth/")
				.httpOnly(true)
				.secure(false)
				.sameSite("Lax")
				.build();
		
		try {
			if(accessToken != null) {
				TokenParseResponseVO parseVO = 
						jwtService.parseAccessToken(accessToken);
				System.out.println("parseVO : " + parseVO);
				empRefreshDao.delete(
						EmpRefreshDto.builder()
							.empNo(parseVO.getEmpNo())
							.userAgent(userAgent)
							.userAddress(req.getRemoteAddr())
						.build()
				);
			}
			else if(refreshToken != null) {
				int empNo = Integer.parseInt(
						jwtService.parseRefreshToken(refreshToken)
						);
				empRefreshDao.delete(
						EmpRefreshDto.builder()
							.empNo(empNo)
							.userAgent(userAgent)
							.userAddress(req.getRemoteAddr())
						.build()
				);
			}
		}
		catch(Exception e) {
			
			return ResponseEntity.noContent()
					.header(
						HttpHeaders.SET_COOKIE,
						accessCookie.toString(),
						refreshCookie.toString()
					)
					.build();
		}
		
		return ResponseEntity.noContent()
					.header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
				.build();
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthLoginResponseVO> refresh(
			@RequestHeader(
					value="User-Agent",
					required=false,
					defaultValue = "UNKNOWN"
			)String userAgent,
			HttpServletRequest req,
			@CookieValue(name = "refreshToken", required = false) String refreshToken
			
			){
		
		//지금 여기 부분 에러 해결하고 나면 원상 복구 해야함
		
		System.out.println("===== REFRESH START =====");
	    System.out.println("refreshToken 존재 : " + (refreshToken != null));
	    System.out.println("userAgent : " + userAgent);
	    System.out.println("address : " + req.getRemoteAddr());
	    
		if(refreshToken == null) {
			System.out.println("실패 : refreshToken 없음");
			throw new WhoAreYouException();
		}
		
		int empNo;
		
		try {
	        empNo = Integer.parseInt(
	            jwtService.parseRefreshToken(refreshToken)
	        );
	    }
	    catch(Exception e) {
	        System.out.println("실패 : refreshToken JWT 검증 실패");
	        throw e;
	    }

	    System.out.println("empNo : " + empNo);
		
		EmpRefreshDto empRefreshDto = 
				empRefreshDao.find(
					EmpRefreshDto.builder()
						.empNo(empNo)
						.userAgent(userAgent)
						.userAddress(req.getRemoteAddr())
					.build()
				);
		
		if(empRefreshDto == null) {
	        System.out.println("실패 : DB refresh 정보 없음");
	        throw new WhoAreYouException();
	    }
		
		if(!empRefreshDto.getTokenValue().equals(refreshToken)) {
	        System.out.println("실패 : DB token과 cookie token 불일치");
	        throw new WhoAreYouException();
	    }
		
		System.out.println("refresh 검증 성공");
		
		EmpDto empDto = empDao.selectOne(empNo);
		
		TokenCreateRequestVO createVO = new TokenCreateRequestVO();
		BeanUtils.copyProperties(empDto, createVO);
		
		String accessToken = jwtService.createAccessToken(createVO);
		String newRefreshToken = jwtService.createRefreshToken(empNo);
		
		ResponseCookie accessCookie = ResponseCookie
				.from("accessToken", accessToken)
				.maxAge(Duration.ofSeconds(jwtProperties.getAccessTokenValidity()))
				.path("/")
				.httpOnly(true)
				.secure(false)
				.sameSite("Lax")
				.build();
		
		ResponseCookie refreshCookie = ResponseCookie
				.from("refreshToken", newRefreshToken)
				.maxAge(Duration.ofSeconds(jwtProperties.getRefreshTokenValidity()))
				.path("/service/auth/")
				.httpOnly(true)
				.secure(false)
				.sameSite("Lax")
				.build();
		
		empRefreshDao.insertOrUpdate(
				EmpRefreshDto.builder()
					.empNo(empNo)
					.userAgent(userAgent)
					.userAddress(req.getRemoteAddr())
					.tokenValue(newRefreshToken)
				.build()
		);
		
		AuthLoginResponseVO response = new AuthLoginResponseVO();
		
		BeanUtils.copyProperties(empDto, response);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
				.body(response);

	}
	
	

}
