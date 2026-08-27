package com.kh.finalprj.vo.task;

import java.util.List;

import com.kh.finalprj.dto.TaskDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "칸반 보드 3단 분류 응답 VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskMoveResponseVO {

    @Schema(description = "할 일(TODO) 목록")
    private List<TaskDto> todoList;

    @Schema(description = "진행 중(IN_PROGRESS) 목록")
    private List<TaskDto> inProgressList;

    @Schema(description = "완료(DONE) 목록")
    private List<TaskDto> doneList;
}