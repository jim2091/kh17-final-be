package com.kh.finalprj.vo.task;

import java.sql.Timestamp;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class TaskUpdateRequestVO {
    private int taskNo;
    private int projectNo;
    private String taskTitle;
    private String taskContent;
    private Integer assignedMemberNo;
    private String taskStatus;
    private String taskPriority;
    private String taskCategory;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp taskStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp taskEnd;

    private List<Integer> collaboratorMemberNos;

    // 👇 수정자 번호 필드 추가 (이 부분이 있어야 쿼리로 넘어갑니다)
    private Integer taskModifierNo;
}