package com.kh.finalprj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthCheckRestController {
	//인증 확인용 API
	//프론트에서 처음 화면 만들때 혹은 새로고침시 로그인 판정할때 localstorage만 확인하고 판정하지 않도록 부를 용도
	@GetMapping("/check")
	public ResponseEntity<Void> check() {
		return ResponseEntity.ok().build();
	}
}
