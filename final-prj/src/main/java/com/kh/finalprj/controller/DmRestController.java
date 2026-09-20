package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.DmDao;
import com.kh.finalprj.dto.DmRoomDto;
import com.kh.finalprj.service.DmService;
import com.kh.finalprj.vo.dm.DmMessageRequestVO;
import com.kh.finalprj.vo.dm.DmMessageResponseVO;
import com.kh.finalprj.vo.dm.DmRoomListResponseVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "DM 서비스")
@CommonsApiResponse
@RestController
@RequestMapping("/api/dm")
public class DmRestController {

	@Autowired
	private DmService dmService;

	//상대방과의 DM방 조회 또는 생성
	@PostMapping("/room/{targetEmpNo}")
	public DmRoomDto openRoom(
			@PathVariable int targetEmpNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {

		return dmService.openRoom(parseVO.getEmpNo(), targetEmpNo);

	}
	
	@GetMapping("/rooms")
	public List<DmRoomListResponseVO> roomList(
			@CurrentUser TokenParseResponseVO parseVO
	) {

		return dmService.roomList(
				parseVO.getEmpNo()
		);

	}
	
	@PostMapping("/room/{roomNo}/messages")
	public DmMessageResponseVO messages(
			@PathVariable int roomNo,
			@CurrentUser TokenParseResponseVO parseVO,
			@Valid @RequestBody DmMessageRequestVO request
	) {

		return dmService.selectMessages(
				roomNo,
				parseVO.getEmpNo(),
				request
		);

	}

}