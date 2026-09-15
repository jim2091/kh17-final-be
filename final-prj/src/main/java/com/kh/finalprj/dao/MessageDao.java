package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.vo.message.ChannelMessageRequestVO;
import com.kh.finalprj.vo.message.MessageSearchRequestVO;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.message.MessageUnreadChannelVO;
import com.kh.finalprj.vo.message.MessageUnreadVO;
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
	
	//메세지 읽음 처리
	int readChannelMessage(int channelNo, int projectMemberNo);
	
	//특정 메세지 안 읽은 사람 수
	int countUnread(int chatMessageNo, int channelNo, int projectMemberNo);
	
	//메시지별 unreadCount 목록
	List<MessageUnreadVO> selectUnreadCount(int channelNo);
	
	//메세지 조회
	MessageVO selectOne(int chatMessageNo);
	
	//채널별 내가 안 읽은 메세지 수
	List<MessageUnreadChannelVO> selectChannelUnreadCount(
			int projectNo, int projectMemberNo);
	
	//채널 메세지 검색
	List<MessageVO> search(int channelNo, MessageSearchRequestVO request);
	
	int searchCount(int channelNo, MessageSearchRequestVO request);
	
	//특정 메세지 주변 대화 조회
	List<MessageVO> selectContext(int channelNo, int chatMessageNo);
}
