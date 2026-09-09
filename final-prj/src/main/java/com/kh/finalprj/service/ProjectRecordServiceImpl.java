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
import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
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
}
