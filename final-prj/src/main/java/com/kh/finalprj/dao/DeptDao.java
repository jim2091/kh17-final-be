package com.kh.finalprj.dao;

import com.kh.finalprj.dto.DeptDto;

public interface DeptDao {
	
	int sequence();
	void insert(DeptDto deptDto);
	
	DeptDto selectOne(int deptNo);

}
