package com.kh.finalprj.dao;

import com.kh.finalprj.dto.EmpDto;


public interface EmpDao {
	
	int sequence();
	void insert(EmpDto empDto);
	
	EmpDto selectOne(int empNo);
	boolean checkAvailableEmail(String empEmail);

}
