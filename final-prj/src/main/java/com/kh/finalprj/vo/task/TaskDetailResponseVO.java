package com.kh.finalprj.vo.task;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kh.finalprj.dto.TaskCollaboDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailResponseVO {

	private int taskNo;
	private int projectNo;
	private String taskTitle;
	private String taskContent;
	// 담당자 정보 (project_member + user_info 조인)
	private Integer assignedMemberNo;
	private String assignedMemberName;
	private String assignedMemberDept;
	// 상태 및 일정
	private String taskStatus;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp taskStart;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp taskEnd;
	private String taskCategory;
	private String taskPriority;
	private int taskProgress;
	private int taskWriterNo;
	private String taskWriterName;
	// 등록/수정 일시
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp taskCtime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp taskUtime;
	// 협업자 상세 목록
	private List<TaskCollaboDto> collaborators;
}