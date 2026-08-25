package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ChannelDto;

public interface ChannelDao {
	//채널 생성
	int sequence();
	void create(ChannelDto channelDto);
	
	//채널 목록
	List<ChannelDto> list(int projectNo);
	
	//채널 상세
	ChannelDto selectOne(int projectNo, int channelNo);
	
	//채널 삭제
	void delete(int projectNo, int channelNo);
	
	//채널 수정
	void update(ChannelDto channelDto);
	
	//프로젝트 번호를 찾는 메소드
	int findProjectNo(int channelNo);
}
