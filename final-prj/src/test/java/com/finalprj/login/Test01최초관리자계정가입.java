package com.finalprj.login;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalprj.FinalPrjApplication;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;

@SpringBootTest(classes = FinalPrjApplication.class)
public class Test01최초관리자계정가입 {
	
	
	
	@Autowired
	private EmpDao empDao;
	
	
	
	
	//사용자 추가 
	@Test
	public void test() {
		int empNo = empDao.sequence();
		String email = "support1@test.com";
		String adminPassword = "test1234!";
		EmpDto empDto = new EmpDto();
		empDto.setEmpNo(empNo);
		empDto.setEmpEmail(email);
		empDto.setEmpPassword(adminPassword);
		empDto.setEmpDeptNo(4);
		empDto.setEmpPositionNo(4);
		empDao.insert(empDto);
		
		
	}
	
}
