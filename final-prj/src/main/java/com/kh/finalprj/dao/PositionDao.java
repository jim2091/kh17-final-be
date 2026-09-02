package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.vo.position.PositionListVO;

public interface PositionDao {
	
	int sequence();
	void insert(PositionDto positionDto);
	
	PositionDto selectOne(int positionNo);
	
	List<PositionListVO> selectList();
	
	boolean updateAll(PositionDto positionDto);

}
