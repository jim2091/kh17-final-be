package com.kh.finalprj.dao;

import java.util.List;
import com.kh.finalprj.dto.TaskCollaboDto;

public interface TaskCollaboDao {

    //단건 협업자 등록
    void add(int taskNo, int projectMemberNo);

    //특정 업무의 협업자 목록 조회
    List<TaskCollaboDto> selectByTaskNo(int taskNo);

    //지정 가능한 협업자 후보 목록 조회 (프로젝트 참여 O, 주 담당자 X, 기존 협업자 X)
    List<TaskCollaboDto> selectAvailableCollaborators(int projectNo, int taskNo);

    //특정 업무의 협업자 전체 삭제 (수정/교체 시 사용)
    boolean deleteByTaskNo(int taskNo);

    //특정 협업자 1명만 단건 삭제
    boolean deleteOne(int taskNo, int projectMemberNo);
}