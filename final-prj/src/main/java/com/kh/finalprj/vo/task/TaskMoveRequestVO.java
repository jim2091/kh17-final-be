package com.kh.finalprj.vo.task;

import lombok.Data;

@Data
public class TaskMoveRequestVO {
    private int taskNo;
    private int projectNo;
    private String targetStatus; // 변경할 상태 ("TODO", "IN_PROGRESS", "DONE")
    private int newOrder;        // 이동할 순서 번호 (1부터 시작)
}