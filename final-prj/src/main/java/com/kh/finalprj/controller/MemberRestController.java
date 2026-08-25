package com.kh.finalprj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.dao.EmpDao;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="회원 정보 관리 서비스")
@AuthApiResponse

@RestController
@RequestMapping("/api/member")
public class MemberRestController {

	@Autowired
	private EmpDao empDao;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
