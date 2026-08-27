package com.kh.finalprj.service;

import java.util.List;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

public interface TaskService {

	// [1] 업무 등록 (TaskDto 매핑 + 협업자 N명 순차 등록)
	int add(TaskAddRequestVO requestVO, List<Integer> collaboratorMemberNos, int empNo);

	// [2] 업무 단건 상세 조회
	TaskDetailResponseVO selectOne(int taskNo);

	// [3] 프로젝트별 전체 업무 목록 조회 (칸반 보드용)
	List<TaskDto> selectByProjectNo(int projectNo);

	// [4] 업무 내용 수정
	boolean update(TaskDto taskDto);

	// [5] 칸반 카드 위치/상태 이동 (드래그 앤 드롭)
	boolean updatePosition(int taskNo, String taskStatus, int position);

	// [6] 업무 삭제
	boolean delete(int taskNo);
}