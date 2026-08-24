package com.kh.finalprj.dao;

import com.kh.finalprj.dto.EmpRefreshDto;

public interface EmpRefreshDao {

	void insertOrUpdate(EmpRefreshDto empRefreshDto);
	void delete(EmpRefreshDto empRefreshDto);
	EmpRefreshDto find(EmpRefreshDto empRefreshDto);
}
