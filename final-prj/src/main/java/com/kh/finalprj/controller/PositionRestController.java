package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.dao.PositionDao;
import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.vo.position.PositionAddRequestVO;
import com.kh.finalprj.vo.position.PositionAddResponseVO;
import com.kh.finalprj.vo.position.PositionListVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="직급 정보 관리 서비스")
@AuthApiResponse

@RestController
@RequestMapping("/api/position")
public class PositionRestController {
	
	@Autowired
	private PositionDao positionDao;
	
	@GetMapping("/")
	public List<PositionListVO> list(){
		return positionDao.selectList();
	}
	
	@PostMapping("/add")
	public PositionAddResponseVO add(@RequestBody PositionAddRequestVO request) {
		
		int positionNo = positionDao.sequence();
		
		PositionDto positionDto = new PositionDto();
		
		positionDto.setPositionNo(positionNo);
		BeanUtils.copyProperties(request, positionDto);
		
		positionDao.insert(positionDto);
		
		PositionDto resultDto = positionDao.selectOne(positionDto.getPositionNo());
		PositionAddResponseVO response = new PositionAddResponseVO();
		BeanUtils.copyProperties(resultDto, response);
		
		return response;
		
	}
	

}
