package com.kh.finalprj.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskCollaboDao;
import com.kh.finalprj.dao.TaskCommentsDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dao.TaskFileDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.dto.TaskCollaboDto;
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
    private TaskCollaboDao taskCollaboDao;

    @Autowired
    private ProjectMemberDao projectMemberDao;

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

        // 댓글 작성 알림 발송 처리 (Set을 이용해 담당자/작성자/협업자 중복 제거 및 본인 제외)
        if (generatedCommentNo > 0) {
            String targetUrl = "/projects/" + task.getProjectNo() + "/kanban?taskNo=" + task.getTaskNo();
            String notiContent = "'" + task.getTaskTitle() + "' 업무에 새 댓글이 등록되었습니다.";

            Set<Integer> receiverEmpNos = new HashSet<>();

            // 1. 주 담당자 사번 수집
            if (task.getAssignedMemberNo() != null && task.getAssignedMemberNo() > 0) {
                ProjectMemberDto assigneeMember = projectMemberDao.findMember2(task.getAssignedMemberNo());
                if (assigneeMember != null && assigneeMember.getEmpNo() != loginEmpNo) {
                    receiverEmpNos.add(assigneeMember.getEmpNo());
                }
            }

            // 2. 업무 최초 작성자 사번 수집
            if (task.getTaskWriterNo() > 0) {
                ProjectMemberDto writerMember = projectMemberDao.findMember2(task.getTaskWriterNo());
                if (writerMember != null && writerMember.getEmpNo() != loginEmpNo) {
                    receiverEmpNos.add(writerMember.getEmpNo());
                }
            }

            // 3. 해당 업무의 협업자들 사번 수집
            List<TaskCollaboDto> collabs = taskCollaboDao.selectByTaskNo(task.getTaskNo());
            if (collabs != null) {
                for (TaskCollaboDto c : collabs) {
                    ProjectMemberDto collabMember = projectMemberDao.findMember2(c.getProjectMemberNo());
                    if (collabMember != null && collabMember.getEmpNo() != loginEmpNo) {
                        receiverEmpNos.add(collabMember.getEmpNo());
                    }
                }
            }

            // 4. 최종 취합된 고유 대상자들에게만 알림 1통씩 발송
            for (int receiverEmpNo : receiverEmpNos) {
                notificationService.send(NotificationDto.builder()
                    .notificationReceiver(receiverEmpNo)
                    .projectNo(task.getProjectNo())
                    .notificationType("TASK_COMMENT")
                    .notificationTarget(task.getTaskNo())
                    .notificationUrl(targetUrl)
                    .notificationContent(notiContent)
                    .build());
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