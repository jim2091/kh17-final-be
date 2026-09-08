package com.kh.finalprj.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kh.finalprj.vo.dept.DeptListSearchVO;
import com.kh.finalprj.vo.dept.DeptListVO;
import com.kh.finalprj.vo.page.PagenationVO;

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
	
	//부서목록 조회(+페이지네이션)
	@PostMapping("/")
	public Map<String, Object> list(@RequestBody PagenationVO pageVO){
		int count = deptDao.count();
		List<DeptListVO> list = deptDao.selectList(pageVO);
		
		Map<String, Object> result = new HashMap<>();
		result.put("count", count);
		result.put("list", list);
		
		return result;
		
	}
	//부서목록 조회(검색용)
	@GetMapping("/search")
	public List<DeptListSearchVO> listSearch(){
		return deptDao.listSearch();
		
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
