package com.kh.finalprj.dao;

import com.kh.finalprj.dto.PositionDto;

public interface PositionDao {
	
	int sequence();
	void insert(PositionDto positionDto);
	
	PositionDto selectOne(int positionNo);

}
