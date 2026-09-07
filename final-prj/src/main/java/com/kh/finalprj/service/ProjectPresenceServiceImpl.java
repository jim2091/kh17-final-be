package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.vo.project.ProjectMemberListResponseVO;
import com.kh.finalprj.websocket.presence.PresenceService;
import com.kh.finalprj.websocket.presence.vo.PresenceResponseVO;

@Service
public class ProjectPresenceServiceImpl implements ProjectPresenceService{

	@Autowired
	private ProjectMemberDao projectMemberDao;
	
	@Autowired
	private PresenceService presenceService;
	
	@Autowired
	private ProjectPermissionService projectPermissionService;
	
	@Override
	public List<PresenceResponseVO> list(int projectNo, int empNo) {
		
		//프로젝트 멤버인지 확인
		projectPermissionService.checkMember(projectNo, empNo);
		
		//프로젝트 멤버 목록 조회
		List<ProjectMemberListResponseVO> memberList = 
				projectMemberDao.selectProjectMemberList(projectNo);
		
		List<PresenceResponseVO> presenceList = memberList.stream()
				.map(member -> PresenceResponseVO.builder()
						.empNo(member.getEmpNo())
						.empName(member.getEmpName())
						.status(presenceService.getStatus(member.getEmpNo()))
						.build())
				.toList();
		
		return presenceList;
	}
}
