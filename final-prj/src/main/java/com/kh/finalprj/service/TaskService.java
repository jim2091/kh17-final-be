package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskMoveRequestVO;
import com.kh.finalprj.vo.task.TaskMoveResponseVO;

public interface TaskService {
    int add(TaskAddRequestVO requestVO, List<Integer> collaboratorMemberNos, int empNo);
    TaskDetailResponseVO selectOne(int taskNo);
    List<TaskDto> selectByProjectNo(int projectNo);
    TaskMoveResponseVO selectKanbanBoard(int projectNo);
    boolean update(TaskDto taskDto);
    boolean moveTask(TaskMoveRequestVO moveVO);
    boolean delete(int taskNo);
}