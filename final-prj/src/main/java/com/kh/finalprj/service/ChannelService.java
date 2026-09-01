package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.ChannelDto;

public interface ChannelService {
	void create(ChannelDto channelDto, int empNo);//채널 생성
	List<ChannelDto> list(int projectNo, int empNo);//채널 목록
	ChannelDto selectOne(int projectNo, int channelNo);//채널 상세
	void delete(int projectNo, int channelNo, int empNo);//채널 삭제
	void update(ChannelDto channelDto, int empNo);//채널 수정
}
