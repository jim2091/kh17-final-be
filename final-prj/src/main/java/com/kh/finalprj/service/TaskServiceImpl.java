package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.TaskCollaboDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskCollaboDao taskCollaboDao;

    @Override
    @Transactional
    public int add(TaskAddRequestVO requestVO, List<Integer> collaboratorMemberNos, int empNo) {
        int generatedTaskNo = taskDao.sequence();

        TaskDto taskDto = TaskDto.builder()
                .taskNo(generatedTaskNo)
                .projectNo(requestVO.getProjectNo())
                .taskTitle(requestVO.getTaskTitle())
                .taskContent(requestVO.getTaskContent())
                .taskWriterNo(empNo) // 👈 세션/토큰의 로그인 사용자 번호 바인딩
                .assignedMemberNo(requestVO.getAssignedMemberNo())
                .taskStatus(requestVO.getTaskStatus() != null ? requestVO.getTaskStatus() : "TODO")
                .taskStart(requestVO.getTaskStart())
                .taskEnd(requestVO.getTaskEnd())
                .taskCategory(requestVO.getTaskCategory())
                .taskPriority(requestVO.getTaskPriority() != null ? requestVO.getTaskPriority() : "MEDIUM")
                .taskProgress(requestVO.getTaskProgress() != null ? requestVO.getTaskProgress() : 0)
                .build();

        taskDao.add(taskDto);

        if (collaboratorMemberNos != null && !collaboratorMemberNos.isEmpty()) {
            for (Integer projectMemberNo : collaboratorMemberNos) {
                if (projectMemberNo != null) {
                    taskCollaboDao.add(generatedTaskNo, projectMemberNo);
                }
            }
        }

        return generatedTaskNo;
    }

    // 업무 단건 상세 조회
    @Override
    @Transactional(readOnly = true)
    public TaskDetailResponseVO selectOne(int taskNo) {
        return taskDao.selectOne(taskNo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> selectByProjectNo(int projectNo) {
        return taskDao.selectByProjectNo(projectNo);
    }

    // 업무 기본 정보 수정
    @Override
    @Transactional
    public boolean update(TaskDto taskDto) {
        return taskDao.update(taskDto);
    }

    @Override
    @Transactional
    public boolean updatePosition(int taskNo, String taskStatus, int position) {
        return taskDao.updatePosition(taskNo, taskStatus, position);
    }

    // 업무 단건 삭제
    @Override
    @Transactional
    public boolean delete(int taskNo) {
        return taskDao.delete(taskNo);
    }
}