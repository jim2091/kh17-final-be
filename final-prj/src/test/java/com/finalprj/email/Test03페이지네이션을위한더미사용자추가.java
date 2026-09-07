package com.finalprj.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalprj.FinalPrjApplication;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.service.EmailService;
import com.kh.finalprj.service.RandomService;

@SpringBootTest(classes = FinalPrjApplication.class)
public class Test03페이지네이션을위한더미사용자추가 {
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private RandomService randomService;
	
	@Autowired
	private EmailService emailService;
	
	@Test
	public void test() {
		for (int i = 100; i <= 200; i++) { 
			int empNo = empDao.sequence(); 
			String tempPassword = randomService.generateString(12); 
			EmpDto empDto = new EmpDto(); // 기본 정보 
			empDto.setEmpNo(empNo); 
			empDto.setEmpEmail("dummy" + i + "@kh.com"); 
			empDto.setEmpName("더미사원"); 
			empDto.setEmpPassword(tempPassword); 
			// 부서 / 직급 
			empDto.setEmpDeptNo(1); 
			empDto.setEmpPositionNo(1); 
			// 추가 정보 
			empDto.setEmpBirth("1990-05-26");
			empDto.setEmpContact("010-1234-" + String.format("%04d", i)); 
			empDto.setEmpPost("06134"); 
			empDto.setEmpAddress1("서울 강남구 테헤란로 " + (100 + i)); 
			empDto.setEmpAddress2("더미빌딩 " + i + "층"); 
			// 회원 등록 
			empDao.insert(empDto); 
			}
		}
			
	}
	


