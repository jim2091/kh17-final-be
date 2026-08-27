package com.kh.finalprj.service;

import java.util.List;
import com.kh.finalprj.dto.TaskCollaboDto;

public interface TaskCollaboService {

    // 1. 단건 협업자 등록
    void add(int taskNo, int projectMemberNo);

    // 2. 다건 협업자 일괄 등록 (List를 받아 반복 호출)
    void addList(int taskNo, List<Integer> projectMemberNos);

    // 3. 특정 업무의 협업자 목록 조회 (이름, 직급, 부서 정보 포함)
    List<TaskCollaboDto> selectByTaskNo(int taskNo);

    // 4. 특정 업무의 협업자 전체 교체 (기존 협업자 삭제 -> 신규 목록 등록)
    void replaceCollaborators(int taskNo, List<Integer> newProjectMemberNos);

    // 5. 특정 협업자 1명만 단건 삭제
    boolean deleteOne(int taskNo, int projectMemberNo);
}