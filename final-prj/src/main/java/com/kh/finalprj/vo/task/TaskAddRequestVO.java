package com.kh.finalprj.vo.task;

import java.sql.Timestamp;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "업무 생성 객체")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskAddRequestVO {
	
	private int taskNo;
	private int projectNo;
	private String taskTitle;
	private String taskContent;
	private int assignedMemberNo;
	private String assignedMemberName;
	private String taskStatus;
	private Timestamp taskStart;
	private Timestamp taskEnd;
	private String taskCategory;
	private String taskPriority;
	private Integer taskProgress;
	private String taskWriterName;
	private Timestamp taskCtime;
	private Timestamp taskUtime;
	
	private List<Integer> collaboratorMemberNos; // 협업자로 등록할 프로젝트 멤버 번호 목록
}
