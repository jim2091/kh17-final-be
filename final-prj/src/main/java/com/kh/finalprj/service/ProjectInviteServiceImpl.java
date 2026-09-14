package com.kh.finalprj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.NotificationDao;
import com.kh.finalprj.dao.ProjectDao;
import com.kh.finalprj.dao.ProjectInviteDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.ProjectInviteDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WhoAreYouException;
import com.kh.finalprj.error.WrongDataException;

@Service
public class ProjectInviteServiceImpl implements ProjectInviteService{
	@Autowired
	private ProjectInviteDao projectInviteDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private NotificationDao notificationDao;
	
	//프로젝트 초대
	@Transactional
	@Override
	public void invite(int projectNo, int senderEmpNo, int receiverEmpNo) {
		//프로젝트 조회
		ProjectDto project = projectDao.selectProject(projectNo);
		
		if(project == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다.");
		}
		
		//종료 프로젝트 초대 불가
		if("closed".equals(project.getProjectStatus())) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다.");
		}
		
		//종료 프로젝트 초대 불가
		if("closed".equals(project.getProjectStatus())) {
			throw new TargetNotfoundException("종료된 프로젝트에는 초대할 수 없습니다.");
		}
		
		//초대한 사람 프로젝트 멤버 조회
		ProjectMemberDto senderMember = projectMemberDao.findMember(projectNo,senderEmpNo);
		
		if(senderMember == null) {
			throw new WhoAreYouException("프로젝트 멤버가 아닙니다.");
		}
		
		//owner,manager만 초대 가능
		String senderRole = senderMember.getProjectMemberRole();
		
		if(
			!"owner".equals(senderRole)
				&&
			!"manager".equals(senderRole)
		) {
			throw new WhoAreYouException("프로젝트 초대 권한이 없습니다.");
		}
		
		//이미 프로젝트 멤버인지 확인
		ProjectMemberDto receiverMember = projectMemberDao.findMember(projectNo, receiverEmpNo);
	
		if(receiverMember != null) {
			throw new WrongDataException("이미 프로젝트에 참여중인 사원입니다.");
		}
		
		//대기에 있는지 확인
		int pendingCount = projectInviteDao.countPending(projectNo, receiverEmpNo);
		
		if(pendingCount >0) {
			throw new WrongDataException("이미 초대중인 사원입니다.");
		}
		
		//초대 번호
		int projectInviteNo = projectInviteDao.sequence();
		//초대 등록
		ProjectInviteDto invite = ProjectInviteDto.builder()
				.projectInviteNo(projectInviteNo)
				.projectNo(projectNo)
				.projectInviteSender(senderEmpNo)
				.projectInviteReceiver(receiverEmpNo)
				.projectInviteStatus("대기")
			.build();
		
		projectInviteDao.add(invite);
		
		//알림 번호
		int notificationNo = notificationDao.sequence();
		//알림 등록
		NotificationDto notification = NotificationDto.builder()
				.notificationNo(notificationNo)
				.notificationReceiver(receiverEmpNo)
				.projectNo(projectNo)
				.notificationType("project_invite")
				.notificationTarget(projectInviteNo)
				.notificationContent(
						project.getProjectName()+"프로젝트에 초대되었습니다."
				)
				.notificationRead("N")
			.build();
		
		notificationDao.insert(notification);
	}

	//프로젝트 수락
	@Transactional
	@Override
	public void accept(int projectInviteNo, int empNo) {
		//초대 조회
		ProjectInviteDto invite = projectInviteDao.find(projectInviteNo);
		if(invite == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트 초대입니다.");
		}
		
		//초대 받은 본인인지 확인
		if(invite.getProjectInviteReceiver() != empNo) {
			throw new WhoAreYouException("본인의 프로젝트 초대가 아닙니다.");
		}
		
		//대기 상태 확인
		if(!"대기".equals(invite.getProjectInviteStatus())) {
			throw new WrongDataException("이미 처리된 프로젝트 초대입니다.");
		}
		
		int projectNo = invite.getProjectNo();
		
		//프로젝트 확인
		ProjectDto project = projectDao.selectProject(projectNo);
		if(project == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다.");
		}
		
		//종료 프로젝트인지 확인
		if("closed".equals(project.getProjectStatus())) {
			throw new WrongDataException("종료된 프로젝트의 초대는 수락할 수 없습니다.");
		}
		
		//이미 멤버인지 확인
		ProjectMemberDto currentMember = projectMemberDao.findMember(projectNo, empNo);
		
		if(currentMember != null) {
			throw new WrongDataException("이미 프로젝트에 참여중입니다.");
		}
		
		//project_member 등록
		int projectMemberNo = projectMemberDao.sequence();
		
		ProjectMemberDto member = ProjectMemberDto.builder()
				.projectMemberNo(projectMemberNo)
				.projectNo(projectNo)
				.empNo(empNo)
				.projectMemberRole("member")
				.projectMemberStatus("active")
			.build();
		
		projectMemberDao.add(member);
		
		//초대 accepted변경
		int updateResult = projectInviteDao.accept(projectInviteNo);
		
		if(updateResult == 0) {
			throw new WrongDataException("프로젝트 초대를 처리할 수 없습니다.");
		}
		
	}
	
	//초대 거절
	@Transactional
	@Override
	public void reject(int projectInviteNo, int empNo) {
		ProjectInviteDto invite = projectInviteDao.find(projectInviteNo);
		
		if(invite == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트 초대입니다.");
		}
		
		//내 초대인지
		if(invite.getProjectInviteReceiver() != empNo) {
			throw new WhoAreYouException("본인의 프로젝트 초대가 아닙니다.");
		}
		
		//대기 확인
		if(!"대기".equals(invite.getProjectInviteStatus())) {
			throw new WrongDataException("이미 처리된 프로젝트 초대입니다.");
		}
		
		//거절 상태 변경
		int updateResult = projectInviteDao.reject(projectInviteNo);
		
		if(updateResult == 0) {
			throw new WrongDataException("프로젝트 초대를 처리할 수 없습니다.");
		}
	}

}
