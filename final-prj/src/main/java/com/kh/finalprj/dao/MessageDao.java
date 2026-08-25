package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.vo.channel.ChannelMessageRequestVO;
import com.kh.finalprj.vo.channel.MessageVO;

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
	
}
