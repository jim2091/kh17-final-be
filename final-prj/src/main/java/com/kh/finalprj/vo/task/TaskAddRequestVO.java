package com.kh.finalprj.vo.task;

import java.sql.Timestamp;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskAddRequestVO {
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
    private Integer taskProgress;
    private String taskWriterName;
    private Timestamp taskCtime;
    private Timestamp taskUtime;
    
    private List<Integer> collaboratorMemberNos;
}