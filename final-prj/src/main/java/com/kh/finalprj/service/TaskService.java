package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskMoveRequestVO;
import com.kh.finalprj.vo.task.TaskMoveResponseVO;
import com.kh.finalprj.vo.task.TaskUpdateRequestVO;

public interface TaskService {

    // 1. 업무 신규 등록 (주 담당자 및 협업자 배정 알림 발송 포함)
    int add(TaskAddRequestVO requestVO, List<Integer> collaboratorMemberNos, int empNo);

    // 2. 업무 단건 상세 조회 (협업자 목록 결합)
    TaskDetailResponseVO selectOne(int taskNo);

    // 3. 프로젝트별 업무 단순 리스트 조회
    List<TaskDto> selectByProjectNo(int projectNo);

    // 4. 칸반 보드 3단 분류 조회 (TODO, IN_PROGRESS, DONE)
    TaskMoveResponseVO selectKanbanBoard(int projectNo);

    // 5. 업무 내용 및 협업자 목록 수정 (드로어 편집 완료용)
    boolean update(TaskUpdateRequestVO updateVO);

    // 6. 업무 기본 정보 단독 수정 (호환용)
    boolean update(TaskDto taskDto);

    // 7. 칸반 카드 드래그 이동 (순서 밀기 + 상태/순서 변경)
    boolean moveTask(TaskMoveRequestVO moveVO);

    // 8. 업무 삭제 (Soft Delete)
    boolean delete(int taskNo);

    // 9. 삭제된 업무 복원 (휴지통 복구)
    boolean restore(int taskNo);

    // 10. 삭제된 업무(휴지통) 목록 조회
    List<TaskDto> selectDeletedByProjectNo(int projectNo);

}