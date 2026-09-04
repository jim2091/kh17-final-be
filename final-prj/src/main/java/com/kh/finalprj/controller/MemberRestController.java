package com.kh.finalprj.controller;

import java.io.IOException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.DeptDao;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dao.PositionDao;
import com.kh.finalprj.dto.DeptDto;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.AttachService;
import com.kh.finalprj.vo.emp.ChangeEmpRequestVO;
import com.kh.finalprj.vo.emp.ChangeEmpResponseVO;
import com.kh.finalprj.vo.emp.EmpMeResponseVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AttachService attachService;
	
	@ApiResponse(responseCode = "200", description = "조회성공")
	@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public EmpMeResponseVO me(
			@CurrentUser TokenParseResponseVO parseVO
			) throws IOException {
//		System.out.println("parseVO : "+ parseVO);
		EmpDto empDto = empDao.selectOne(parseVO.getEmpNo());
		
		if(empDto == null) throw new TargetNotfoundException();
		
		DeptDto deptDto = deptDao.selectOne(empDto.getEmpDeptNo());
		
		PositionDto positionDto = positionDao.selectOne(empDto.getEmpPositionNo());
		
		//프로필 사진 조회
		Integer attachNo = empDao.findAttachNumber(parseVO.getEmpNo());
		
		EmpMeResponseVO response = new EmpMeResponseVO();
		
		BeanUtils.copyProperties(empDto, response);
		
		response.setDeptName(deptDto.getDeptName());
		response.setPositionName(positionDto.getPositionName());
		response.setAttachNo(attachNo);
		
		return response;
		
	}
	
	
	//사용자 정보 수정(본인) + 프로필 사진 추가 or 수정
	@Transactional
	@PutMapping(
			value = "/"
			,produces="application/json"
			,consumes="multipart/form-data"
			)
	public ChangeEmpResponseVO updateAll(
			@CurrentUser TokenParseResponseVO parseVO,
//			@Valid @RequestBody ChangeEmpRequestVO request
			@RequestPart(value="empProfile", required=false) MultipartFile empProfile,
			@RequestPart(value="emp") ChangeEmpRequestVO request
			
			) throws IllegalStateException, IOException {
//		System.out.println("request : "+ request);
		//기존 정보 조회
		EmpDto empDto = empDao.selectOne(parseVO.getEmpNo());
		if(empDto == null) throw new TargetNotfoundException();
		
		//기존 비밀번호와 일치하는지 검증 
		boolean passwordValid = passwordEncoder.matches(
				request.getPrevEmpPassword(),
				empDto.getEmpPassword()
				);
		
		if(passwordValid == false) {
			return ChangeEmpResponseVO.builder()
						.status(false)
						.message("비밀번호가 일치하지 않습니다")
					.build();
					
		}
		
		//새 비밀번호와 새 비밀번호 확인이 일치하는지 검증 
		if(request.getNewEmpPassword1() != null && !request.getNewEmpPassword1().isBlank()) {
			boolean passwordConfirm = request.getNewEmpPassword1().equals(request.getNewEmpPassword2());
			if(passwordConfirm == false) {
				return ChangeEmpResponseVO.builder()
						.status(false)
						.message("입력하신 새 비밀번호가 서로 달라요")
						.build();
			
			}
		}
		
		
	
		
		//정보 갈아끼우기
		empDto.setEmpPassword(request.getNewEmpPassword1());
		BeanUtils.copyProperties(request, empDto);
		
		//수정처리
		empDao.updateAll(empDto);
		
		
		
		//프로필 있으면 등록후 사원정보와 연결
		String source = "프로필";
		if(empProfile != null && empProfile.isEmpty() == false) {
			int attachNo = attachService.save(empProfile, empDto.getEmpName(), source);
			empDao.connect(empDto.getEmpNo(), attachNo);
		}
		
		
		
		
		return ChangeEmpResponseVO.builder()
					.status(true)
					.message("회원 정보 변경이 완료되었습니다.")
				.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
