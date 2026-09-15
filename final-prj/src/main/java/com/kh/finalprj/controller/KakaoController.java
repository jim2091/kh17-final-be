package com.kh.finalprj.controller;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.configuration.JwtProperties;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dao.EmpRefreshDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.EmpRefreshDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.service.KakaoService;
import com.kh.finalprj.vo.auth.AuthLoginResponseVO;
import com.kh.finalprj.vo.jwt.TokenCreateRequestVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.kakao.KakaoLoginRequestVO;
import com.kh.finalprj.vo.kakao.KakaoTokenResponseVO;
import com.kh.finalprj.vo.kakao.KakaoUserInfoResponseVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoController {
	
	@Autowired
	private KakaoService kakaoService;
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private JwtProperties jwtProperties;
	
	@Autowired
	private EmpRefreshDao empRefreshDao;
	
	@GetMapping("/connect")
	public String kakaoConnect() throws Exception {
		return "redirect:"+kakaoService.kakaoConnect();
	}
	
	@GetMapping("/login")
	public String kakaoLogin() {
		return "redirect:"+kakaoService.kakaoLogin();
	}

	
	@GetMapping("/callback")
//	@ResponseBody
	public String kakaoCallback(
			@RequestParam("code") String code,
			@CurrentUser TokenParseResponseVO parseVo
			) {
//		System.out.println("카카오 인증 코드 : "+ code);
		
		KakaoTokenResponseVO token = kakaoService.getKakaoToken(code);
		
//		System.out.println("카카오 토큰 : "+ token);
		
		KakaoUserInfoResponseVO userInfo = kakaoService.getKakaoUserInfo(token.getAccessToken());
		
//		System.out.println("카카오 id : " + userInfo.getId());
		
		Long id = userInfo.getId();
		
		
		
		//emp_kakao에 insert
		KakaoLoginRequestVO request = new KakaoLoginRequestVO();
		request.setId(id);
		request.setEmpNo(parseVo.getEmpNo());
		empDao.kakao(request);
		
		return "redirect:http://localhost:5173/me";
	}
	
	@GetMapping("/login/callback")
	public String kakaoLoginCallback(
			@RequestParam("code") String code,
			@RequestHeader(
					value = "User-Agent",
					required = false,
					defaultValue = "UNKNOWN"
			) String userAgent,
			HttpServletRequest req,
			HttpServletResponse res
			) {
		
		KakaoTokenResponseVO token = kakaoService.getKakaoToken2(code);
		
		KakaoUserInfoResponseVO userInfo = kakaoService.getKakaoUserInfo(token.getAccessToken());
		
		Long id = userInfo.getId();
		
		Integer empNo = empDao.findEmpNoByKakaoId(id);
		
		if(empNo == null) 
			throw new TargetNotfoundException();
		
		EmpDto empDto = empDao.selectOne(empNo);
		

		
		Integer attachNo = empDao.findAttachNumber(empDto.getEmpNo());
		
		AuthLoginResponseVO response = AuthLoginResponseVO.builder()
					.empNo(empDto.getEmpNo())
					.empEmail(empDto.getEmpEmail())
					.empName(empDto.getEmpName())
					.empLevel(empDto.getEmpLevel())
					.attachNo(attachNo)
				.build();
		
		TokenCreateRequestVO tokenRequest = new TokenCreateRequestVO();
		BeanUtils.copyProperties(response, tokenRequest);
		
		String accessToken = jwtService.createAccessToken(tokenRequest);
		String refreshToken = jwtService.createRefreshToken(tokenRequest.getEmpNo());
		
		//로그인 세션 식별값 생성
		String sessionId = UUID.randomUUID().toString();
		
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
				ResponseCookie sessionCookie = ResponseCookie
						.from("sessionId", sessionId)
						//refresh 토큰과 같은 시간으로
						.maxAge(Duration.ofSeconds(
								jwtProperties.getRefreshTokenValidity()
						))
						.path("/service/auth/")
						.httpOnly(true)
						.secure(false)
						.sameSite("Lax")
						.build();
		
		empRefreshDao.insertOrUpdate(
				EmpRefreshDto.builder()
					.empNo(response.getEmpNo())
					.sessionId(sessionId)
					.userAgent(userAgent)
					.userAddress(req.getRemoteAddr())
					.tokenValue(refreshToken)
				.build()
		);
//		ResponseEntity.ok()
//		//쿠키를 추가하는 설정
//			.header(
//					HttpHeaders.SET_COOKIE, 
//					accessCookie.toString(),
//					refreshCookie.toString(),
//					sessionCookie.toString()
//					)
//			.body(response);
		
		res.addHeader(
	            HttpHeaders.SET_COOKIE,
	            accessCookie.toString()
	    );

	    res.addHeader(
	            HttpHeaders.SET_COOKIE,
	            refreshCookie.toString()
	    );

	    res.addHeader(
	            HttpHeaders.SET_COOKIE,
	            sessionCookie.toString()
	    );
		
		
		
		return "redirect:http://localhost:5173/";
		
	}
	
	
	


	
	
	
	
	
	
	
	
	
	
	
	

}
