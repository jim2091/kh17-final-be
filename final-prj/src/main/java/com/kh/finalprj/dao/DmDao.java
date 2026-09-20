package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.DmRoomDto;
import com.kh.finalprj.vo.dm.DmMessageRequestVO;
import com.kh.finalprj.vo.dm.DmMessageVO;
import com.kh.finalprj.vo.dm.DmRoomListResponseVO;


public interface DmDao {

	//DM방 번호 발급
	int roomSequence();

	//두 사람 사이 기존 DM방 조회
	DmRoomDto findRoom(int emp1No, int emp2No);

	//DM방 생성
	void createRoom(DmRoomDto dmRoomDto);
	
	//방 번호로 DM방 조회
	DmRoomDto findRoomByNo(int roomNo);
	
	//내 DM방 목록
	List<DmRoomListResponseVO> roomList(int empNo);

	//DM 메시지 목록 조회
	List<DmMessageVO> selectMessages(int roomNo, DmMessageRequestVO request);

	//조회 가능한 남은 메세지 수
	int countMessages(int roomNo, DmMessageRequestVO request);
	
	//DM 메세지 번호 발급
	int messageSequence();

	//DM 메세지 등록
	void addMessage(DmMessageVO message);

	//DM 메세지 단건 조회
	DmMessageVO selectMessage(int messageNo);

}