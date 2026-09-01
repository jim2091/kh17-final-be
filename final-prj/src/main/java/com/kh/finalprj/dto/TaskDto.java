package com.kh.finalprj.dto;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "업무 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private int taskNo;
    private int projectNo;
    private String taskTitle;
    private String taskContent;
    private Integer assignedMemberNo;
    private String assignedMemberName;
    private String taskStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp taskStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp taskEnd;

    private String taskCategory;
    private String taskPriority;
    private int taskProgress;
    private int taskOrder;
    private int taskWriterNo; 
    private String taskWriterName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp taskCtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp taskUtime;
}