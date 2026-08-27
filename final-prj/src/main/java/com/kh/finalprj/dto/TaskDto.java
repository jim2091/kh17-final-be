package com.kh.finalprj.dto;

import java.sql.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "업무 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskDto {
    private int taskNo;
    private int projectNo;
    private String taskTitle;
    private String taskContent;
    private Integer assignedMemberNo; // 미배정(NULL) 허용이므로 Integer 
    private String taskStatus;        // TODO, IN_PROGRESS, DONE
    private Timestamp taskStart;
    private Timestamp taskEnd;
    private String taskCategory;
    private String taskPriority;      // 낮음, 보통, 높음, 긴급
    private int taskProgress;         // 0 ~ 100
    private int taskWriterNo;         // 작성자 (project_member_no)
    private Timestamp taskCtime;
    private Timestamp taskUtime;
}