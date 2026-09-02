package com.kh.finalprj.vo.task;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="업무 협업자 상세 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor	
public class TaskCollaboratorResponseVO {
	private int taskNo;
	private int projectMemberNo;
	private Timestamp taskCollaboratorCtime;
	private int empNo;
	private String memberName;
	private String deptName;
	private String jobPosition;
	private String projectMemberJob;
}
