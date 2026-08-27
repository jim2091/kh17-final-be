package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.vo.message.ChannelMessageRequestVO;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.message.MessageUpdateRequestVO;
import com.kh.finalprj.vo.message.MessageVO;

public interface MessageDao {
	//메세지 번호
	int sequence();
	
	//메세지 등록
	void add(MessageVO message);
	
	//과거 메세지
	List<MessageVO> selectList(
			int channelNo, ChannelMessageRequestVO request);
	
	//더보기 여부
	int count(int channelNo, ChannelMessageRequestVO request);
	
	//메세지 작업을 위한 정보 조회
	MessageTargetVO selectTarget(int chatMessageNo);
	
	//메세지 삭제
	void delete(int chatMessageNo);
	
	//메세지 수정
	void update(int chatMessageNo, String chatMessageContent);
}
