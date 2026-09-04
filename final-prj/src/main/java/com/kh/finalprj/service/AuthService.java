package com.kh.finalprj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.finalprj.configuration.LoginProperties;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.auth.AuthLoginRequestVO;
import com.kh.finalprj.vo.auth.AuthLoginResponseVO;

@Service
public class AuthService {
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public AuthLoginResponseVO login(AuthLoginRequestVO request) {
		EmpDto empDto = empDao.selectOne(request.getEmpEmail());
		
		if(empDto == null) {
			throw new TargetNotfoundException();
		}
		
		boolean valid = passwordEncoder.matches(
				request.getEmpPassword()
				, empDto.getEmpPassword());
		
		if(valid == false) {
			throw new TargetNotfoundException();
		}
		
		if(empDto.getEmpState().equals("inactive")) {
			throw new GetOutException();
		}
		
		Integer attachNo = empDao.findAttachNumber(empDto.getEmpNo());
		
		
		return AuthLoginResponseVO.builder()
					.empNo(empDto.getEmpNo())
					.empEmail(empDto.getEmpEmail())
					.empName(empDto.getEmpName())
					.empLevel(empDto.getEmpLevel())
					.attachNo(attachNo)
				.build();
	}

}
