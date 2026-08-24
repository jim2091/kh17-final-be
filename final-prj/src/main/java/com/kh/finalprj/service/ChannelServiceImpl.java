package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dto.ChannelDto;
import com.kh.finalprj.error.TargetNotfoundException;

@Service
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	private ChannelDao channelDao;

	//채널 생성 
	@Override
	public void create(ChannelDto channelDto) {	
		//(1) 현재 사용자가 해당 프로젝트의 멤버인지 확인
		
		//(2) project_member_no 조회
		
		//(3) 채널 번호 발급
		int channelNo = channelDao.sequence();
		
		//(4) DTO 생성
		channelDto.setChatChannelNo(channelNo);
		
		//(5) 채널 생성
		channelDao.insert(channelDto);
	}

	//채널 목록
	@Override
	public List<ChannelDto> list(int projectNo) {
		return channelDao.list(projectNo);
	}

	//채널 상세
	@Override
	public ChannelDto selectOne(int projectNo, int channelNo) {
		return channelDao.selectOne(projectNo, channelNo);
	}

	//채널 삭제
	@Override
	public void delete(int projectNo, int channelNo) {
		//(1) 채널 존재 여부 확인
		ChannelDto channelDto = channelDao.selectOne(projectNo, channelNo);
		
		if(channelDto == null) {
			throw new TargetNotfoundException();
		}
		
		//(2) 채널 삭제
		channelDao.delete(projectNo, channelNo);
	}

	//채널 수정
	@Override
	public void update(ChannelDto channelDto) {
		//(1) 채널 존재 여부 확인
		ChannelDto target = channelDao.selectOne(
			channelDto.getProjectNo(), channelDto.getChatChannelNo());
		
		if(channelDto == null) {
			throw new TargetNotfoundException();
		}
		
		//(2) 채널 수정
		channelDao.update(channelDto);
	}
}
