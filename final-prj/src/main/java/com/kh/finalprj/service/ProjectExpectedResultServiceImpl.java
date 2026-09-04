//package com.kh.finalprj.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.kh.finalprj.dao.ProjectExpectedResultDao;
//import com.kh.finalprj.dao.ProjectMemberDao;
//import com.kh.finalprj.dto.ProjectExpectedResultDto;
//import com.kh.finalprj.error.TargetNotfoundException;
//import com.kh.finalprj.error.WhoAreYouException;
//import com.kh.finalprj.error.WrongDataException;
//import com.kh.finalprj.vo.project.ProjectExpectedResultRequestVO;
//
//@Repository
//public class ProjectExpectedResultServiceImpl implements ProjectExpectedResultService{
//	@Autowired
//	private ProjectMemberDao projectMemberDao;
//	@Autowired
//	private ProjectExpectedResultDao projectExpectedResultDao;
//	
//	//기대결과 목록
//	@Override
//	public List<ProjectExpectedResultDto> resultList(int projectNo, int empNo) {
//		//프로젝트 참여자 확인
//		String role = projectMemberDao.selectRole(projectNo,empNo);
//		
//		if(role == null) {
//			throw new WhoAreYouException("프로젝트 참여자가 아닙니다.");
//		}
//		return projectExpectedResultDao.selectList(projectNo);
//	}
//
//	//기대결과 등록
//	@Transactional
//	@Override
//	public void addResult(int projectNo, ProjectExpectedResultRequestVO requestVO, int empNo) {
//		//1.권한 확인
//		String role = projectMemberDao.selectRole(projectNo, empNo);
//		if(!role.equals("owner")) {
//			throw new WhoAreYouException("기대결과 등록 권한이 없습니다.");
//		}
//		
//		//2.내용 검사
//		if(requestVO.getProjectResultContent() == null ||
//			requestVO.getProjectResultContent().isBlank()
//		) {
//			throw new WrongDataException("기대결과를 입력해주세요.");
//		}
//		
//		//3.번호 발급
//		int projectResultNo = projectExpectedResultDao.sequence();
//		//4.순서 발급
//		int projectResultOrder = projectExpectedResultDao.nextOrder(projectNo);
//		//5.DTO생성
//		ProjectExpectedResultDto projectExpectedResultDto = ProjectExpectedResultDto.builder()
//				.projectResultNo(projectResultNo)
//				.projectNo(projectNo)
//				.projectResultContent(requestVO.getProjectResultContent().trim())
//				.projectResultOrder(projectResultOrder)
//			.build();
//		//6.등록
//		projectExpectedResultDao.add(projectExpectedResultDto);
//	}
//
//	//기대결과 수정
//	@Transactional
//	@Override
//	public void updateResult(ProjectExpectedResultRequestVO requestVO, int empNo, int projectNo, int projectResultNo) {
//		//1.owner확인
//		String role = projectMemberDao.selectRole(projectNo, empNo);
//		
//		if(!role.equals("owner")) {
//			throw new WhoAreYouException("기대결과 수정 권한이 없습니다.");
//		}
//		
//		//2.내용 검사
//		if(requestVO.getProjectResultContent() == null ||
//			requestVO.getProjectResultContent().isBlank()
//		) {
//			throw new WrongDataException("기대결과를 입력해주세요.");
//		}
//		
//		//3.수정
//		boolean result = projectExpectedResultDao.update(
//				projectNo, projectResultNo, 
//				requestVO.getProjectResultContent().trim()
//		);
//		
//		if(result == false) {
//			throw new TargetNotfoundException("기대결과를 찾을 수 없습니다.");
//		}
//	}
//
//	//기대결과 삭제
//	@Transactional
//	@Override
//	public void deleteResult(int projectNo, int projectResultNo, int empNo) {
//		//1.owner확인
//		String role = projectMemberDao.selectRole(projectNo, empNo);
//		
//		if(!role.equals("owner")) {
//			throw new WhoAreYouException("기대결과 수정 권한이 없습니다.");
//		}
//		
//		//2.삭제
//		boolean result = projectExpectedResultDao.delete(projectNo, projectResultNo);
//		
//		if(result == false) {
//			throw new TargetNotfoundException("기대결과를 찾을 수 없습니다.");			
//		}
//>>>>>>> refs/remotes/origin/main
//	}
//
//}
