package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.dao.DeptDao;
import com.kh.finalprj.dto.DeptDto;
import com.kh.finalprj.vo.dept.DeptAddRequestVO;
import com.kh.finalprj.vo.dept.DeptAddResponseVO;
import com.kh.finalprj.vo.dept.DeptChangeRequestVO;
import com.kh.finalprj.vo.dept.DeptChangeResponseVO;
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
	
	//부서등록
	@PostMapping("/add")
	public DeptAddResponseVO add(@RequestBody DeptAddRequestVO request) {
		
		int deptNo = deptDao.sequence();
//		System.out.println("deptNo : "+ deptNo);
		
		DeptDto deptDto = new DeptDto();
		
		deptDto.setDeptNo(deptNo);
		
		int resultNo = deptDto.getDeptNo();
//		System.out.println("resultNo : " + resultNo);
		
		BeanUtils.copyProperties(request, deptDto);
//		System.out.println("deptDto : " + deptDto);
		deptDao.insert(deptDto);
		
		DeptDto resultDto = deptDao.selectOne(deptDto.getDeptNo());
		DeptAddResponseVO response = new DeptAddResponseVO();
		BeanUtils.copyProperties(resultDto, response);
		
		return response;
	}
	
	//부서 수정
	@PutMapping("/edit")
	public DeptChangeResponseVO edit(@RequestBody DeptChangeRequestVO request) {
		DeptDto deptDto = new DeptDto();
		BeanUtils.copyProperties(request, deptDto);
		deptDao.updateAll(deptDto);

		DeptDto resultDto = deptDao.selectOne(deptDto.getDeptNo());
		DeptChangeResponseVO response = new DeptChangeResponseVO();
		BeanUtils.copyProperties(resultDto, response);
		
		return response;
		
	}
		

}
