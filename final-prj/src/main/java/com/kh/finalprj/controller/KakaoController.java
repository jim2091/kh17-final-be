package com.kh.finalprj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.service.KakaoService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.kakao.KakaoLoginRequestVO;
import com.kh.finalprj.vo.kakao.KakaoTokenResponseVO;
import com.kh.finalprj.vo.kakao.KakaoUserInfoResponseVO;

@Controller
public class KakaoController {
	
	@Autowired
	private KakaoService kakaoService;
	
	@Autowired
	private EmpDao empDao;
	
	@GetMapping("/oauth/kakao/login")
	public String kakaoLogin() throws Exception {
		return "redirect:"+kakaoService.kakaoLogin();
	}
	
	@GetMapping("/oauth/kakao/callback")
//	@ResponseBody
	public String kakaoCallback(
			@RequestParam("code") String code,
			@CurrentUser TokenParseResponseVO parseVo
			) {
		System.out.println("카카오 인증 코드 : "+ code);
		
		KakaoTokenResponseVO token = kakaoService.getKakaoToken(code);
		
		System.out.println("카카오 토큰 : "+ token);
		
		KakaoUserInfoResponseVO userInfo = kakaoService.getKakaoUserInfo(token.getAccessToken());
		
		
		Long id = userInfo.getId();
		
		//emp_kakao에 insert
		KakaoLoginRequestVO request = new KakaoLoginRequestVO();
		request.setId(id);
		request.setEmpNo(parseVo.getEmpNo());
		empDao.kakao(request);
		
		return "redirect:http://localhost:5173/me";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
