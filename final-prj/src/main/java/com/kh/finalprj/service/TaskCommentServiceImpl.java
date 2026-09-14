package com.kh.finalprj.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskCommentsDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dao.TaskFileDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

@Service
public class TaskCommentServiceImpl implements TaskCommentService {

    @Autowired
    private TaskCommentsDao taskCommentsDao;

    @Autowired
    private TaskFileDao taskFileDao;

    @Autowired
    private AttachDao attachDao;

    @Autowired
    private AttachService attachService;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ProjectMemberDao projectMemberDao;

    // 알림 서비스 주입
    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public int add(TaskCommentDto taskCommentDto, int loginEmpNo) {
        TaskDetailResponseVO task = taskDao.selectOne(taskCommentDto.getTaskNo());
        if (task == null) throw new IllegalArgumentException("존재하지 않는 업무입니다.");
        
        Integer projectMemberNo = projectMemberDao.findProjectMemberNo(task.getProjectNo(), loginEmpNo);
        if (projectMemberNo == null) throw new IllegalStateException("해당 프로젝트에 참여 중인 멤버가 아닙니다.");
        taskCommentDto.setProjectMemberNo(projectMemberNo);
        
        if (taskCommentDto.getTaskCommentContent() == null || taskCommentDto.getTaskCommentContent().trim().isEmpty()) {
            taskCommentDto.setTaskCommentContent("(파일 첨부)");
        }
        
        int generatedCommentNo = taskCommentsDao.sequence();
        taskCommentDto.setTaskCommentNo(generatedCommentNo);
        taskCommentsDao.add(taskCommentDto);

        // 댓글 작성 알림 발송 처리
        if (generatedCommentNo > 0) {
            String targetUrl = "/projects/" + task.getProjectNo() + "/kanban?taskNo=" + task.getTaskNo();
            String notiContent = "'" + task.getTaskTitle() + "' 업무에 새 댓글이 등록되었습니다.";

            // 1. 업무 담당자에게 알림 발송 (본인이 아닐 때만)
            if (task.getAssignedMemberNo() != null && task.getAssignedMemberNo() > 0) {
                ProjectMemberDto assigneeMember = projectMemberDao.findMember(task.getAssignedMemberNo());
                if (assigneeMember != null && assigneeMember.getEmpNo() != loginEmpNo) {
                    notificationService.send(NotificationDto.builder()
                        .notificationReceiver(assigneeMember.getEmpNo())
                        .projectNo(task.getProjectNo())
                        .notificationType("TASK_COMMENT")
                        .notificationTarget(task.getTaskNo())
                        .notificationUrl(targetUrl)
                        .notificationContent(notiContent)
                        .build());
                }
            }

            // 2. 업무 최초 작성자에게 알림 발송 (담당자와 다르고, 본인이 아닐 때만)
            if (task.getTaskWriterNo() > 0 && (task.getAssignedMemberNo() == null || task.getTaskWriterNo() != task.getAssignedMemberNo())) {
                ProjectMemberDto writerMember = projectMemberDao.findMember(task.getTaskWriterNo());
                if (writerMember != null && writerMember.getEmpNo() != loginEmpNo) {
                    notificationService.send(NotificationDto.builder()
                        .notificationReceiver(writerMember.getEmpNo())
                        .projectNo(task.getProjectNo())
                        .notificationType("TASK_COMMENT")
                        .notificationTarget(task.getTaskNo())
                        .notificationUrl(targetUrl)
                        .notificationContent(notiContent)
                        .build());
                }
            }
        }

        return generatedCommentNo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskCommentDto> findComments(int taskNo) {
        return taskCommentsDao.findComments(taskNo);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskCommentDetailResponseVO selectOne(int taskCommentNo) {
        return taskCommentsDao.selectOne(taskCommentNo);
    }

    @Override
    @Transactional
    public boolean update(TaskCommentDto taskCommentDto) {
        if (taskCommentDto.getTaskCommentContent() == null || taskCommentDto.getTaskCommentContent().trim().isEmpty()) {
            taskCommentDto.setTaskCommentContent("(파일 첨부)");
        }
        return taskCommentsDao.update(taskCommentDto);
    }

    @Override
    @Transactional
    public boolean delete(int taskCommentNo) {
        List<TaskFileResponseVO> files = taskFileDao.selectFilesByCommentNo(taskCommentNo);
        if (files != null && !files.isEmpty()) {
            for (TaskFileResponseVO file : files) {
                taskFileDao.deleteCommentFile(taskCommentNo, file.getAttachNo());
                AttachDto attachDto = attachDao.selectOne(file.getAttachNo());
                String uploader = (attachDto != null && attachDto.getAttachUploader() != null) ? attachDto.getAttachUploader() : "SYSTEM";
                attachService.delete(file.getAttachNo(), uploader);
            }
        }
        return taskCommentsDao.delete(taskCommentNo);
    }
}