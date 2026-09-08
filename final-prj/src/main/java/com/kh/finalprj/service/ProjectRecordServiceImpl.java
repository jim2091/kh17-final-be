package com.kh.finalprj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ProjectRecordDao;
import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;
import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;

@Service
public class ProjectRecordServiceImpl implements ProjectRecordService{
	
	@Autowired
	private ProjectRecordDao projectRecordDao;
	
	@Autowired
	private ProjectPermissionService projectPermissionService;
	
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
		
		return ProjectRecordAddResponseVO.builder()
					.projectRecordNo(projectRecordNo)
				.build();
	}
}
