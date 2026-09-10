package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.MessageDao;
import com.kh.finalprj.dao.NoteDao;
import com.kh.finalprj.dao.ProjectRecordDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WrongDataException;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordEditRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordIssueResolveRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

@Service
public class ProjectRecordServiceImpl implements ProjectRecordService{
	
	@Autowired
	private ProjectRecordDao projectRecordDao;
	
	@Autowired
	private ProjectPermissionService projectPermissionService;
	
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private AttachDao attachDao;
	
	@Override
	@Transactional
	public ProjectRecordAddResponseVO add(int projectNo, int empNo, ProjectRecordAddRequestVO requestVO) {
		
		int projectMemberNo = projectPermissionService.findProjectMemberNo(projectNo, empNo);
		
		int projectRecordNo = projectRecordDao.sequence();
		
		ProjectRecordDto projectRecordDto = ProjectRecordDto.builder()
					.projectRecordNo(projectRecordNo)
					.projectNo(projectNo)
					.projectRecordWriterNo(projectMemberNo)
					.projectRecordType(requestVO.getProjectRecordType())
					.projectRecordTitle(requestVO.getProjectRecordTitle())
					.projectRecordContent(requestVO.getProjectRecordContent())
				.build();
		
		projectRecordDao.insert(projectRecordDto);
		
		if("ISSUE".equals(requestVO.getProjectRecordType())) {
			ProjectRecordIssueDto projectRecordIssueDto = ProjectRecordIssueDto.builder()
						.projectRecordNo(projectRecordNo)
					.build();
			
			projectRecordDao.insertIssue(projectRecordIssueDto);
		}
		
		//관련 원본 연결
		//task
		if(requestVO.getTaskNoList() != null) {
			for(int taskNo : requestVO.getTaskNoList()) {
				
				TaskDetailResponseVO taskDto = taskDao.selectOne(taskNo);
				//존재하는지, 같은 프로젝트인지 검사
				if(taskDto == null)
					throw new TargetNotfoundException();
				if(taskDto.getProjectNo() != projectNo)
					throw new GetOutException();
				
				projectRecordDao.insertTask(projectRecordNo, taskNo);
			}
		}
		//chat
		if(requestVO.getChatMessageNoList() != null) {
			for(int chatMessageNo : requestVO.getChatMessageNoList()) {
				
				MessageTargetVO messageTarget = messageDao.selectTarget(chatMessageNo);
				
				if(messageTarget == null)
					throw new TargetNotfoundException();
				if(messageTarget.getProjectNo() != projectNo)
					throw new GetOutException();
				
				projectRecordDao.insertMessage(projectRecordNo, chatMessageNo);
			}
		}
		//note
		if(requestVO.getNoteNoList() != null) {
			for(int noteNo : requestVO.getNoteNoList()) {
				
				NoteDetailResponseVO note = noteDao.selectOne(noteNo);
				
				if(note == null)
					throw new TargetNotfoundException();
				if(note.getProjectNo() != projectNo)
					throw new GetOutException();
				
				projectRecordDao.insertNote(projectRecordNo, noteNo);
			}
		}
		
		if(requestVO.getAttachNoList() != null) {
			for(int attachNo : requestVO.getAttachNoList()) {
				
				List<AttachDto> projectFileList = attachDao.selectListByProject(projectNo);
				//지금 attach쪽 손대고 있는거 같아서 얘만 좀 특이한 형태로
				boolean exists = projectFileList.stream()
						.anyMatch(
							attach -> 
								attach.getAttachNo() == attachNo
						);
				
				if(exists == false)
					throw new TargetNotfoundException();
				
				projectRecordDao.insertAttach(projectRecordNo, attachNo);
			}
		}
		return ProjectRecordAddResponseVO.builder()
					.projectRecordNo(projectRecordNo)
				.build();
	}
	
	@Override
	public List<ProjectRecordListResponseVO> list(int projectNo, int empNo) {
		
		projectPermissionService.findProjectMemberNo(projectNo, empNo);
		
		return projectRecordDao.list(projectNo);
	}
	
	@Override
	public ProjectRecordDetailResponseVO detail(int projectRecordNo, int empNo) {
		
		ProjectRecordDetailResponseVO response = projectRecordDao.detail(projectRecordNo);
		
		if(response == null)
			throw new TargetNotfoundException();
		
		projectPermissionService.findProjectMemberNo(response.getProjectNo(), empNo);
		
		response.setRelatedList(projectRecordDao.selectRelatedList(projectRecordNo));
		
		return response;
	}
	
	@Override
	@Transactional
	public void edit(int projectRecordNo, int empNo, ProjectRecordEditRequestVO request) {
		
		//수정 대상 record 조회
		ProjectRecordDetailResponseVO target = projectRecordDao.detail(projectRecordNo);
		
		if(target == null)
			throw new TargetNotfoundException();
		
		int projectNo = target.getProjectNo();
		
		//현재 로그인 사용자의 프로젝트 멤버 정보 조회
		ProjectMemberDto projectMemberDto = projectPermissionService.findMember(target.getProjectNo(), empNo);
		
		//권한 확인
		boolean writer = target.getProjectRecordWriterNo() == projectMemberDto.getProjectMemberNo();
		
		String role = projectMemberDto.getProjectMemberRole();
		
		boolean ownerOrManager = "owner".equals(role) || "manager".equals(role);
		
		if(!writer && !ownerOrManager)
			throw new GetOutException();
		
		//원본 task 연결 수정
		if(request.getTaskNoList() != null) {
			//새로 연결할 task 검증
			for(int taskNo : request.getTaskNoList()) {
				TaskDetailResponseVO task = taskDao.selectOne(taskNo);
				
				if(task == null) throw new TargetNotfoundException();
				if(task.getProjectNo() != projectNo) throw new GetOutException();
			}
			
			//기존 연결 삭제 / 새 연결 등록
			projectRecordDao.deleteTaskList(projectRecordNo);
			
			for(int taskNo : request.getTaskNoList()) {
				projectRecordDao.insertTask(projectRecordNo, taskNo);
			}
		}
		
		//원본 note 연결 수정
		if(request.getNoteNoList() != null) {
			for(int noteNo : request.getNoteNoList()) {
				NoteDetailResponseVO note = noteDao.selectOne(noteNo);
				
				if(note == null) throw new TargetNotfoundException();
				if(note.getProjectNo() != projectNo) throw new GetOutException();
			}
			
			projectRecordDao.deleteNoteList(projectRecordNo);
			for(int noteNo : request.getNoteNoList()) {
				projectRecordDao.insertNote(projectRecordNo, noteNo);
			}
		}
		
		//원본 attach 연결 수정
		if(request.getAttachNoList() != null) {
			List<AttachDto> projectFileList = attachDao.selectListByProject(projectNo);
			
			for(int attachNo : request.getAttachNoList()) {
				boolean exists = 
						projectFileList.stream()
							.anyMatch(attach -> attach.getAttachNo() == attachNo);
				
				if(!exists) throw new TargetNotfoundException();
			}
			
			projectRecordDao.deleteAttachList(projectRecordNo);
			for(int attachNo : request.getAttachNoList()) {
				projectRecordDao.insertAttach(projectRecordNo, attachNo);
			}
		}
		
		
		
		//수정할 데이터 생성
		ProjectRecordDto projectRecordDto = ProjectRecordDto.builder()
					.projectRecordNo(projectRecordNo)
					.projectRecordTitle(request.getProjectRecordTitle())
					.projectRecordContent(request.getProjectRecordContent())
					.projectRecordModifierNo(projectMemberDto.getProjectMemberNo())
				.build();
		
		//수정
		boolean result = projectRecordDao.edit(projectRecordDto);
		
		if(!result)
			throw new TargetNotfoundException();
		
	}
	
	@Override
	@Transactional
	public void delete(int projectRecordNo, int empNo) {
		
		//삭제할 record 조회
		ProjectRecordDetailResponseVO target = projectRecordDao.detail(projectRecordNo);
		
		if(target == null)
			throw new TargetNotfoundException();
		
		//로그인 사용자 프로젝트 멤버 정보 조회
		ProjectMemberDto projectMemberDto = projectPermissionService.findMember(target.getProjectNo(), empNo);
		
		//삭제 권한
		boolean writer = target.getProjectRecordWriterNo() == projectMemberDto.getProjectMemberNo();
		
		String role = projectMemberDto.getProjectMemberRole();
		
		boolean ownerOrManager = "owner".equals(role) || "manager".equals(role);
		
		if(!writer && !ownerOrManager)
			throw new GetOutException();
		
		//삭제
		boolean result = projectRecordDao.delete(projectRecordNo);
		
		if(!result)
			throw new TargetNotfoundException();
		
	}
	
	@Override
	@Transactional
	public void resolveIssue(int projectRecordNo, int empNo, ProjectRecordIssueResolveRequestVO request) {
		
		//record 조회
		ProjectRecordDetailResponseVO target = projectRecordDao.detail(projectRecordNo);
		
		if(target == null)
			throw new TargetNotfoundException();
		
		//issue인지 확인
		if(!"ISSUE".equals(target.getProjectRecordType()))
			throw new WrongDataException();
		
		//로그인 사용자 프로젝트 멤버 정보 조회
		ProjectMemberDto projectMemberDto = projectPermissionService.findMember(target.getProjectNo(), empNo);
		
		//권한
		boolean writer = target.getProjectRecordWriterNo() == projectMemberDto.getProjectMemberNo();
		
		String role = projectMemberDto.getProjectMemberRole();
		
		boolean ownerOrManager = "owner".equals(role) || "manager".equals(role);
		
		if(!writer && !ownerOrManager)
			throw new GetOutException();
		
		//이슈 해결
		ProjectRecordIssueDto issueDto = ProjectRecordIssueDto.builder()
					.projectRecordNo(projectRecordNo)
					.issueResolution(request.getProjectRecordIssueResolution())
				.build();
		
		boolean result = projectRecordDao.resolveIssue(issueDto);
		
		if(!result)
			throw new WrongDataException();
		
		//최종 수정자/수정시간 갱신
		ProjectRecordDto recordDto = ProjectRecordDto.builder()
					.projectRecordNo(projectRecordNo)
					.projectRecordModifierNo(projectMemberDto.getProjectMemberNo())
				.build();
		
		projectRecordDao.updateModifier(recordDto);
	}
	
	@Override
	@Transactional
	public void reopenIssue(int projectRecordNo, int empNo) {
		ProjectRecordDetailResponseVO target = projectRecordDao.detail(projectRecordNo);
		
		if(target == null)
			throw new TargetNotfoundException();
		
		if(!"ISSUE".equals(target.getProjectRecordType()))
			throw new WrongDataException();
		
		ProjectMemberDto projectMemberDto = projectPermissionService.findMember(target.getProjectNo(), empNo);
		
		boolean writer = target.getProjectRecordWriterNo() == projectMemberDto.getProjectMemberNo();
		
		String role = projectMemberDto.getProjectMemberRole();
		
		boolean ownerOrManager = "owner".equals(role) || "manager".equals(role);
		
		if(!writer && !ownerOrManager)
			throw new GetOutException();
		
		boolean result = projectRecordDao.reopenIssue(projectRecordNo);
		
		if(!result)
			throw new WrongDataException();
		
		ProjectRecordDto recordDto = ProjectRecordDto.builder()
					.projectRecordNo(projectRecordNo)
					.projectRecordModifierNo(projectMemberDto.getProjectMemberNo())
				.build();
		
		projectRecordDao.updateModifier(recordDto);
		
	}
}
