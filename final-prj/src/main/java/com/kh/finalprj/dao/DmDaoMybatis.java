package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.DmRoomDto;
import com.kh.finalprj.vo.dm.DmMessageRequestVO;
import com.kh.finalprj.vo.dm.DmMessageVO;
import com.kh.finalprj.vo.dm.DmRoomListResponseVO;


@Repository
public class DmDaoMybatis implements DmDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int roomSequence() {
		return sqlSession.selectOne("mapper.dm.roomSequence");
	}


	@Override
	public DmRoomDto findRoom(int emp1No, int emp2No) {
		
		DmRoomDto dmRoomDto =
				DmRoomDto.builder()
					.dmRoomEmp1No(emp1No)
					.dmRoomEmp2No(emp2No)
				.build();

		return sqlSession.selectOne("mapper.dm.findRoom", dmRoomDto);

	}

	@Override
	public void createRoom(DmRoomDto dmRoomDto) {
		sqlSession.insert("mapper.dm.createRoom", dmRoomDto);
	}
	
	@Override
	public DmRoomDto findRoomByNo(int roomNo) {
		return sqlSession.selectOne("mapper.dm.findRoomByNo", roomNo);
	}

	@Override
	public List<DmRoomListResponseVO> roomList(
			int empNo
	) {

		return sqlSession.selectList(
				"mapper.dm.roomList",
				empNo
		);

	}

	@Override
	public List<DmMessageVO> selectMessages(int roomNo, DmMessageRequestVO request) {

		Map<String, Object> params = new HashMap<>();
		
		params.put("roomNo", roomNo);
		params.put("size", request.getSize());
		params.put("lastMessageNo", request.getLastMessageNo());

		return sqlSession.selectList("mapper.dm.selectMessages", params);
	}

	@Override
	public int countMessages(int roomNo, DmMessageRequestVO request) {

		Map<String, Object> params = new HashMap<>();

		params.put("roomNo", roomNo);
		params.put("lastMessageNo", request.getLastMessageNo());

		return sqlSession.selectOne("mapper.dm.countMessages", params);

	}
	
	@Override
	public int messageSequence() {

		return sqlSession.selectOne("mapper.dm.messageSequence");
	}


	@Override
	public void addMessage(DmMessageVO message) {
		sqlSession.insert("mapper.dm.addMessage", message);
	}


	@Override
	public DmMessageVO selectMessage(int messageNo) {
		return sqlSession.selectOne("mapper.dm.selectMessage", messageNo);
	}

}