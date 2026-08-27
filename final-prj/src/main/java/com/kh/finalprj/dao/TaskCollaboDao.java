package com.kh.finalprj.dao;

import java.util.List;
import com.kh.finalprj.dto.TaskCollaboDto;

public interface TaskCollaboDao {
    // 1. 단건 협업자 등록
    void add(int taskNo, int projectMemberNo);

    // 2. 업무별 협업자 목록 조회
    List<TaskCollaboDto> selectByTaskNo(int taskNo);

    // 3. 업무별 협업자 전체 삭제 (수정/교체 시 사용)
    boolean deleteByTaskNo(int taskNo);

    // 4. 특정 협업자 1명만 삭제
    boolean deleteOne(int taskNo, int projectMemberNo);
}