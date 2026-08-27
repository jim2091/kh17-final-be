package com.kh.finalprj.dto;

import java.sql.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "업무 협업자 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskCollaboDto {
    private int taskNo;
    private int projectMemberNo;
    private Timestamp taskCollaboratorCtime;

    // 조인 조회용 필드 (화면 표시용)
    private int empNo;
    private String memberName;
    private String deptName;
    private String jobPosition;
}