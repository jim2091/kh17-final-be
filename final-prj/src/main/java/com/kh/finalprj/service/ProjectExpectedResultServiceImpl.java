package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dao.ProjectExpectedResultDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.error.WhoAreYouException;
import com.kh.finalprj.vo.project.ProjectExpectedResultUpdateRequestVO;
@Repository
public class ProjectExpectedResultServiceImpl implements ProjectExpectedResultService{
	
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private ProjectExpectedResultDao projectExpectedResultDao;

	//프로젝트 기대결과 목록
	@Override
	public List<ProjectExpectedResultDto> resultList(int projectNo, int empNo) {
		
		//프로젝트 참여자인지 확인
		String role = projectMemberDao.selectRole(projectNo, empNo);
		
		if(role == null) {
			throw new WhoAreYouException("프로젝트 참가자가 아닙니다.");
		}
		return projectExpectedResultDao.selectList(projectNo);
	}
	//프로젝트 기대결과 생성
	@Override
	public void resultAdd(int projectNo, int empNo) {
		
	}
	@Override
	public void resultUpdate(int projectNo, int projectResultNo, int empNo,
			ProjectExpectedResultUpdateRequestVO requestVO) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resultDelete(int projectNo, int projectResultNo, int empNo) {
		// TODO Auto-generated method stub
		
	}

}
