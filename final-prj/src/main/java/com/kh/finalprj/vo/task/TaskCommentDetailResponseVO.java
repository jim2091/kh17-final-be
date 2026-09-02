package com.kh.finalprj.vo.task;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="댓글 정보 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskCommentDetailResponseVO {
	private int taskCommentNo;
	private int taskNo;
	private int projectMemberNo;
	private String taskCommentContent;
	private Timestamp taskCommentCtime;
	private Timestamp taskCommentUtime;
	
	private int empNo;
	private String empName;
	private String empDeptNo;
	private String empPositionNo;
}
