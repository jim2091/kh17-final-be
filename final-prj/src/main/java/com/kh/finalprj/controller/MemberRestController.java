package com.kh.finalprj.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.DeptDao;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dao.PositionDao;
import com.kh.finalprj.dto.DeptDto;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.member.EmpMeResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="회원 정보 관리 서비스")
@AuthApiResponse

@RestController
@RequestMapping("/api/member")
public class MemberRestController {

	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private DeptDao deptDao;
	
	@Autowired
	private PositionDao positionDao;
	
	@ApiResponse(responseCode = "200", description = "조회성공")
	@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public EmpMeResponseVO me(
			@CurrentUser TokenParseResponseVO parseVO
			) {
		EmpDto empDto = empDao.selectOne(parseVO.getEmpNo());
		
		if(empDto == null) throw new TargetNotfoundException();
		
		DeptDto deptDto = deptDao.selectOne(empDto.getEmpDeptNo());
		
		PositionDto positionDto = positionDao.selectOne(empDto.getEmpPositionNo());
		
		EmpMeResponseVO response = new EmpMeResponseVO();
		
		BeanUtils.copyProperties(empDto, response);
		
		response.setDeptName(deptDto.getDeptName());
		response.setPositionName(positionDto.getPositionName());
		
		return response;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
