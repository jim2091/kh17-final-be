package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

@Repository
public class TaskDaoMybatis implements TaskDao {

	@Autowired
	private SqlSession sqlSession;

	// 1. 업무 시퀀스 번호 단독 채번 (select task_seq.nextval from dual)
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.task.sequence");
	}

	// 2. 업무 등록 (C)
	@Override
	public int add(TaskDto taskDto) {
		return sqlSession.insert("mapper.task.add", taskDto);
	}

	// 3. 단건 업무 상세 조회 (R)
	@Override
	public TaskDetailResponseVO selectOne(int taskNo) {
		return sqlSession.selectOne("mapper.task.selectOne", taskNo);
	}

	// 4. 프로젝트별 전체 업무 목록 조회 (R - 칸반 보드 로딩용)
	@Override
	public List<TaskDto> selectByProjectNo(int projectNo) {
		return sqlSession.selectList("mapper.task.selectByProjectNo", projectNo);
	}

	// 5. 업무 기본 정보 수정 (U)
	@Override
	public boolean update(TaskDto taskDto) {
		return sqlSession.update("mapper.task.update", taskDto) > 0;
	}

	// 6. 칸반 카드 위치/컬럼 드래그 이동 갱신 (U)
	@Override
	public boolean updatePosition(int taskNo, String columnStatus, int position) {
		Map<String, Object> params = new HashMap<>();
		params.put("taskNo", taskNo);
		params.put("columnStatus", columnStatus);
		params.put("position", position);

		return sqlSession.update("mapper.task.updatePosition", params) > 0;
	}

	// 7. 업무 단건 삭제 (D)
	@Override
	public boolean delete(int taskNo) {
		return sqlSession.delete("mapper.task.delete", taskNo) > 0;
	}
}
