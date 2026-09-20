package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.DmRoomDto;
import com.kh.finalprj.vo.dm.DmMessageRequestVO;
import com.kh.finalprj.vo.dm.DmMessageResponseVO;
import com.kh.finalprj.vo.dm.DmMessageVO;
import com.kh.finalprj.vo.dm.DmRoomListResponseVO;


public interface DmService {

	//상대방과의 DM방 조회 또는 생성
	DmRoomDto openRoom(int empNo, int targetEmpNo);
	
	DmRoomDto findRoom(int roomNo, int empNo);
	
	List<DmRoomListResponseVO> roomList(
			int empNo
	);

	DmMessageResponseVO selectMessages(int roomNo, int empNo, DmMessageRequestVO request);

	DmMessageVO addMessage(int roomNo, int empNo, String content);
}