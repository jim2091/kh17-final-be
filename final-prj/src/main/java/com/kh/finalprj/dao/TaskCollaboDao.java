package com.kh.finalprj.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.kh.finalprj.dto.TaskCollaboDto;

@Mapper
public interface TaskCollaboDao {

    // 1. 단건 협업자 등록
    void add(@Param("taskNo") int taskNo, 
             @Param("projectMemberNo") int projectMemberNo);

    // 2. 특정 업무의 협업자 목록 조회
    List<TaskCollaboDto> selectByTaskNo(@Param("taskNo") int taskNo);

    // 3. [추가] 지정 가능한 협업자 후보 목록 조회 (3단계 필터링 적용)
    List<TaskCollaboDto> selectAvailableCollaborators(@Param("projectNo") int projectNo, 
                                                      @Param("taskNo") int taskNo);

    // 4. 특정 업무의 협업자 전체 삭제 (수정/교체 시 사용)
    boolean deleteByTaskNo(@Param("taskNo") int taskNo);

    // 5. 특정 협업자 1명만 삭제
    boolean deleteOne(@Param("taskNo") int taskNo, 
                      @Param("projectMemberNo") int projectMemberNo);
}