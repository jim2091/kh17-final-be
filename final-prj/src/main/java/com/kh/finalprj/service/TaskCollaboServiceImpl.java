package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.TaskCollaboDao;
import com.kh.finalprj.dto.TaskCollaboDto;

@Service
public class TaskCollaboServiceImpl implements TaskCollaboService {

	@Autowired
	private TaskCollaboDao taskCollaboDao;

	// 1. 단건 협업자 등록
	@Override
	@Transactional
	public void add(int taskNo, int projectMemberNo) {
		taskCollaboDao.add(taskNo, projectMemberNo);
	}

	// 2. 다건 협업자 일괄 등록
	@Override
	@Transactional
	public void addList(int taskNo, List<Integer> projectMemberNos) {
		if (projectMemberNos == null || projectMemberNos.isEmpty())
			return;

		for (Integer projectMemberNo : projectMemberNos) {
			if (projectMemberNo != null) {
				taskCollaboDao.add(taskNo, projectMemberNo);
			}
		}
	}

	// 3. 특정 업무의 협업자 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<TaskCollaboDto> selectByTaskNo(int taskNo) {
		return taskCollaboDao.selectByTaskNo(taskNo);
	}

	// 4. 협업자 전체 교체 (기존 삭제 후 신규 삽입)
	@Override
	@Transactional
	public void replaceCollaborators(int taskNo, List<Integer> newProjectMemberNos) {
		// 1) 기존 협업자 전체 삭제
		taskCollaboDao.deleteByTaskNo(taskNo);

		// 2) 신규 협업자 목록 재삽입
		if (newProjectMemberNos != null && !newProjectMemberNos.isEmpty()) {
			for (Integer projectMemberNo : newProjectMemberNos) {
				if (projectMemberNo != null) {
					taskCollaboDao.add(taskNo, projectMemberNo);
				}
			}
		}
	}

	// 5. 특정 협업자 1명만 삭제
	@Override
	@Transactional
	public boolean deleteOne(int taskNo, int projectMemberNo) {
		return taskCollaboDao.deleteOne(taskNo, projectMemberNo);
	}
}