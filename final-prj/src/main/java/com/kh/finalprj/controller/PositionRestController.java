package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.AuthApiResponse;
import com.kh.finalprj.dao.PositionDao;
import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.vo.position.PositionAddRequestVO;
import com.kh.finalprj.vo.position.PositionAddResponseVO;
import com.kh.finalprj.vo.position.PositionChangeRequestVO;
import com.kh.finalprj.vo.position.PositionChangeResponseVO;
import com.kh.finalprj.vo.position.PositionListVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="직급 정보 관리 서비스")
@AuthApiResponse

@RestController
@RequestMapping("/api/position")
public class PositionRestController {
	
	@Autowired
	private PositionDao positionDao;
	
	@ApiResponse(responseCode = "200", description = "조회성공")
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
	
	//직급수정
	@PutMapping("/edit")
	public PositionChangeResponseVO edit(@RequestBody PositionChangeRequestVO request) {
		
		PositionDto positionDto = new PositionDto();
		BeanUtils.copyProperties(request, positionDto);
		positionDao.updateAll(positionDto);
		
		PositionDto resultDto = positionDao.selectOne(positionDto.getPositionNo());
		PositionChangeResponseVO response = new PositionChangeResponseVO();
		BeanUtils.copyProperties(resultDto, response);
		
		return response;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
