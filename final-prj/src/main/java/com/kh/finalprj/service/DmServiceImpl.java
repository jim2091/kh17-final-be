package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.DmDao;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.DmRoomDto;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WrongDataException;
import com.kh.finalprj.vo.dm.DmMessageRequestVO;
import com.kh.finalprj.vo.dm.DmMessageResponseVO;
import com.kh.finalprj.vo.dm.DmMessageVO;
import com.kh.finalprj.vo.dm.DmRoomListResponseVO;


@Service
public class DmServiceImpl implements DmService {

	@Autowired
	private DmDao dmDao;

	@Autowired
	private EmpDao empDao;


	//상대방과의 DM방 조회 또는 생성
	@Transactional
	@Override
	public DmRoomDto openRoom(int empNo, int targetEmpNo) {
		//자기 자신과 DM 불가
		if(empNo == targetEmpNo)
			throw new IllegalArgumentException("자기 자신과 DM을 생성할 수 없습니다.");
		

		//상대방 존재 여부 확인
		EmpDto target = empDao.selectOne(targetEmpNo);

		if(target == null) 
			throw new TargetNotfoundException("존재하지 않는 사용자입니다.");

		//항상 작은 번호가 emp1,
		//큰 번호가 emp2가 되도록 정렬
		int emp1No = Math.min(empNo, targetEmpNo);

		int emp2No = Math.max(empNo, targetEmpNo);

		//기존 DM방 조회
		DmRoomDto room = dmDao.findRoom(emp1No, emp2No);

		//이미 방이 있으면 그대로 반환
		if(room != null) return room;

		//새 DM방 번호 발급
		int roomNo = dmDao.roomSequence();

		//새 DM방 생성
		DmRoomDto newRoom = DmRoomDto.builder()
									.dmRoomNo(roomNo)
									.dmRoomEmp1No(emp1No)
									.dmRoomEmp2No(emp2No)
								.build();

		dmDao.createRoom(newRoom);

		//생성된 방 반환
		return dmDao.findRoom(emp1No, emp2No);
	}
	
	@Override
	public DmRoomDto findRoom(int roomNo, int empNo) {

		//DM방 조회
		DmRoomDto room =dmDao.findRoomByNo(roomNo);

		//존재하지 않는 방
		if(room == null) 
			throw new TargetNotfoundException();

		//현재 사용자가 이 DM방 참여자인지 확인
		boolean member = room.getDmRoomEmp1No() == empNo
						|| room.getDmRoomEmp2No() == empNo;

		if(member == false) 
			throw new GetOutException();
	
		return room;
	}
	
	@Override
	public List<DmRoomListResponseVO> roomList(
			int empNo
	) {

		return dmDao.roomList(
				empNo
		);

	}
	
	@Override
	public DmMessageResponseVO selectMessages(
			int roomNo,
			int empNo,
			DmMessageRequestVO request
	) {

		//[1] DM방 존재 여부 + 참여자 확인
		findRoom(roomNo, empNo);

		//[2] 메세지 목록 조회
		List<DmMessageVO> messages = dmDao.selectMessages(roomNo, request);

		//[3] 조회 가능한 전체 메세지 개수
		int count = dmDao.countMessages(roomNo, request);

		//[4] 마지막 목록 여부
		boolean last = messages.size() >= count;

		//[5] 응답 생성
		return DmMessageResponseVO.builder()
					.messages(messages)
					.last(last)
				.build();

	}
	
	@Transactional
	@Override
	public DmMessageVO addMessage(
			int roomNo,
			int empNo,
			String content
	) {

		//[1] DM방 존재 여부 + 현재 사용자 참여 여부 확인
		findRoom(roomNo, empNo);


		//[2] 빈 메세지 방지
		if(content == null	|| content.trim().isEmpty())
			throw new WrongDataException("메세지 내용이 없습니다.");

		//[3] DB 컬럼 길이 초과 방지
		if(content.length() > 2000)
			throw new WrongDataException("메세지는 2000자를 초과할 수 없습니다.");

		//[4] 메세지 번호 발급
		int messageNo = dmDao.messageSequence();


		//[5] 메세지 생성
		DmMessageVO message = DmMessageVO.builder()
									.no(messageNo)
									.roomNo(roomNo)
									.senderNo(empNo)
									.content(content)
								.build();

		//[6] 메세지 등록
		dmDao.addMessage(message);

		//[7] 등록된 메세지 다시 조회
		return dmDao.selectMessage(messageNo);

	}

}