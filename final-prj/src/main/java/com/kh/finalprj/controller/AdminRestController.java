package com.kh.finalprj.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.service.EmailService;
import com.kh.finalprj.service.RandomService;
import com.kh.finalprj.vo.admin.AdminComplexSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminComplexSearchResponseVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchResponseVO;
import com.kh.finalprj.vo.admin.EmpAddRequestVO;
import com.kh.finalprj.vo.admin.EmpAddResponseVO;
import com.kh.finalprj.vo.emp.EmpListVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;

@Tag(name = "관리자API")

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private RandomService randomService;
	
	
	//사용자 추가 -> 회원가입 이메일 발송(+임시비밀번호)
	@PostMapping("/add")
	public EmpAddResponseVO add(@RequestBody EmpAddRequestVO request) throws IOException, MessagingException {
//		System.out.println("request : "+ request);
		int empNo = empDao.sequence();
//		System.out.println(empNo);
		String tempPassword = randomService.generateString(12);
		EmpDto empDto = new EmpDto();
//		System.out.println("empDto : " +  empDto);
		empDto.setEmpNo(empNo);
		empDto.setEmpEmail(request.getEmpEmail());
		empDto.setEmpName(request.getEmpName());
		empDto.setEmpPassword(tempPassword);
		empDto.setEmpDeptNo(request.getEmpDeptNo());
		empDto.setEmpPositionNo(request.getEmpPositionNo());
		
//		BeanUtils.copyProperties(request, empDto);
		empDao.insert(empDto);
		emailService.invite(request.getEmpEmail(), tempPassword);
		
		EmpDto resultDto = empDao.selectOne(empDto.getEmpNo());
		EmpAddResponseVO response = new EmpAddResponseVO();
		BeanUtils.copyProperties(resultDto, response);
		
		return response;
	}
	
	// 이메일 중복검사-사용 가능하면 true, 불가능하면 false를 반환
	@GetMapping("/check-email/{empEmail}")
	public boolean checkEmpEmail(@PathVariable String empEmail) {
		return empDao.checkAvailableEmail(empEmail);
	}
	
	//회원목록 조회(번호순)
	@GetMapping("/")
	public List<EmpListVO> list(){
		return empDao.selectList();
		
	}
	
	//회원 복합 검색 결과 조회 
	@PostMapping("/complexSearch")
	public List<AdminComplexSearchResponseVO> list(@RequestBody AdminComplexSearchRequestVO request ){
//		System.out.println("검색 요청 데이터 : "+request);
//		System.out.println("검색 응답 데이터 :"+ empDao.complexSearch(request));
		return empDao.complexSearch(request);
	}
	
	//회원 초성 검색 결과 조회
	@PostMapping("/initial")
	public List<AdminInitialSearchResponseVO> initial(@RequestBody AdminInitialSearchRequestVO request){
		
		return empDao.initialSearch(request);
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	

}