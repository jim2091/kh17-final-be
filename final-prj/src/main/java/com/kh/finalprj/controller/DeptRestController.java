package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.dao.DeptDao;
import com.kh.finalprj.vo.dept.DeptListVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="부서 정보 관리 서비스")
@AuthApiResponse

@RestController
@RequestMapping("/api/dept")
public class DeptRestController {
	
	@Autowired
	private DeptDao deptDao;
	
	//부서목록 조회(번호순)
	@GetMapping("/")
	public List<DeptListVO> list(){
		return deptDao.selectList();
		
	}
		

}
