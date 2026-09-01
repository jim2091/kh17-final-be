package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ChannelDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;

@Service
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private ProjectPermissionService projectPermissionService;
	
	private ProjectMemberDto checkChannelManager(int projectNo, int empNo) {
		//(1) 회원 조회
		ProjectMemberDto projectMemberDto = 
				projectMemberDao.findMember(projectNo, empNo);
		if(projectMemberDto == null) {
			throw new TargetNotfoundException();
		}
		
		//(2) 권한 조회
		String role = projectMemberDto.getProjectMemberRole();
		if(!"owner".equalsIgnoreCase(role)
	            && !"manager".equalsIgnoreCase(role)) {
	        throw new GetOutException();
	    }
		
		return projectMemberDto;
	}

	//채널 생성 
	@Transactional
	@Override
	public void create(ChannelDto channelDto, int empNo) {	
		//(1) OWNER / MANAGER 권한 확인
		ProjectMemberDto projectMemberDto = 
				checkChannelManager(
						channelDto.getProjectNo(), empNo);
		
		//(2) 채널 생성자의 project_member_no 설정
		channelDto.setChatChannelCreator(
				projectMemberDto.getProjectMemberNo());
		
		//(3) 채널 번호 발급
		int channelNo = channelDao.sequence();
		channelDto.setChatChannelNo(channelNo);
		
		//(4) 채널명에 '#' 자동 추가
		String channelName = channelDto.getChatChannelName();
		if(!channelName.startsWith("#")) {
			channelDto.setChatChannelName("#"+channelName);
		}
		
		//(5) 채널 생성
		channelDao.create(channelDto);
	}

	//채널 목록
	@Override
	public List<ChannelDto> list(int projectNo, int empNo) {
		//현재 프로젝트 참여자인지 검사코드 추가
		projectPermissionService.checkMember(projectNo, empNo);
		return channelDao.list(projectNo);
	}

	//채널 상세
	@Override
	public ChannelDto selectOne(int projectNo, int channelNo) {
		return channelDao.selectOne(projectNo, channelNo);
	}

	//채널 삭제
	@Transactional
	@Override
	public void delete(int projectNo, int channelNo, int empNo) {
		//(1) OWNER / MANAGER 권한 확인
		checkChannelManager(projectNo, empNo);
		
		//(2) 채널 존재 여부 확인
		ChannelDto channelDto = channelDao.selectOne(projectNo, channelNo);
		if(channelDto == null) {
			throw new TargetNotfoundException();
		}
		
		//(3) #general 삭제 방지
		if ("#general".equalsIgnoreCase(channelDto.getChatChannelName())) {
		    throw new GetOutException();
		}
		
		//(4) 채널 삭제
		channelDao.delete(projectNo, channelNo);
	}

	//채널 수정
	@Transactional
	@Override
	public void update(ChannelDto channelDto, int empNo) {
		//(1) OWNER / MANAGER 권한 확인
		checkChannelManager(channelDto.getProjectNo(), empNo);
				
		//(2) 채널 존재 여부 확인
		ChannelDto target = channelDao.selectOne(
				channelDto.getProjectNo(), channelDto.getChatChannelNo());
		if(target == null) {
			throw new TargetNotfoundException();
		}
		
		//(3) #general 수정 방지
		if ("#general".equalsIgnoreCase(target.getChatChannelName())) {
		    throw new GetOutException();
		}
		
		//(4) 채널명에 '#' 자동 추가
		String channelName = channelDto.getChatChannelName();
		if(!channelName.startsWith("#")) {
			channelDto.setChatChannelName("#"+channelName);
		}
		
		//(5) 채널 수정
		channelDao.update(channelDto);
	}
}
