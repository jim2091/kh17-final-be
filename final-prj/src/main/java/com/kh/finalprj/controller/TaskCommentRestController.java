package com.kh.finalprj.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.service.NotificationService;
import com.kh.finalprj.service.TaskCommentService;
import com.kh.finalprj.service.TaskService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "업무 댓글 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/task/comment")
public class TaskCommentRestController {

    @Autowired
    private TaskCommentService taskCommentService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectMemberDao projectMemberDao;

    @Operation(summary = "댓글 목록 조회")
    @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")
    @GetMapping(value = "/list/{taskNo}", produces = "application/json")
    public List<TaskCommentDto> list(@PathVariable int taskNo) {
        return taskCommentService.findComments(taskNo);
    }

    @Operation(summary = "댓글 단건 상세 조회")
    @ApiResponse(responseCode = "200", description = "댓글 상세 조회 성공")
    @GetMapping(value = "/{taskCommentNo}", produces = "application/json")
    public TaskCommentDetailResponseVO detail(@PathVariable int taskCommentNo) {
        return taskCommentService.selectOne(taskCommentNo);
    }

    @Operation(summary = "댓글 등록")
    @ApiResponse(responseCode = "200", description = "댓글 등록 성공")
    @PostMapping(value = "/", produces = "application/json")
    public int add(
            @RequestBody TaskCommentDto taskCommentDto,
            @RequestParam(required = false, defaultValue = "0") int projectNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 1;
        int generatedCommentNo = taskCommentService.add(taskCommentDto, loginEmpNo);

        if (generatedCommentNo > 0 && projectNo > 0) {
            // 기존 칸반 댓글창 동기화 브로드캐스트 전송
            simpMessagingTemplate.convertAndSend(
                "/public/projects/" + projectNo + "/kanban",
                Map.of(
                    "eventType", "COMMENT_ADDED",
                    "projectNo", projectNo,
                    "taskNo", taskCommentDto.getTaskNo(),
                    "commentNo", generatedCommentNo,
                    "senderEmpNo", loginEmpNo
                )
            );

            // 업무 정보를 조회하여 주 담당자 및 업무 작성자에게 개인 알림 발송
            TaskDetailResponseVO task = taskService.selectOne(taskCommentDto.getTaskNo());
            if (task != null) {
                Integer assigneeEmpNo = null;

                // 주 담당자 사번 조회
                if (task.getAssignedMemberNo() > 0) {
                    ProjectMemberDto assigneeMember = projectMemberDao.findMember(task.getAssignedMemberNo());
                    if (assigneeMember != null) {
                        assigneeEmpNo = assigneeMember.getEmpNo();
                    }
                }

                // 주 담당자가 본인이 아닐 때 주 담당자에게 알림 전송
                if (assigneeEmpNo != null && assigneeEmpNo != loginEmpNo) {
                    notificationService.send(NotificationDto.builder()
                        .notificationReceiver(assigneeEmpNo)
                        .projectNo(projectNo)
                        .notificationType("TASK_COMMENT")
                        .notificationTarget(taskCommentDto.getTaskNo())
                        .notificationUrl("/projects/" + projectNo + "/kanban?taskNo=" + taskCommentDto.getTaskNo())
                        .notificationContent("'" + task.getTaskTitle() + "' 업무에 새 댓글이 등록되었습니다.")
                        .build());
                }

                // 업무 작성자가 본인이 아니고 주 담당자와도 다를 때 작성자에게 알림 전송
                ProjectMemberDto writerMember = projectMemberDao.findMember(task.getTaskWriterNo());
                if (writerMember != null) {
                    int writerEmpNo = writerMember.getEmpNo();
                    if (writerEmpNo != loginEmpNo && (assigneeEmpNo == null || writerEmpNo != assigneeEmpNo)) {
                        notificationService.send(NotificationDto.builder()
                            .notificationReceiver(writerEmpNo)
                            .projectNo(projectNo)
                            .notificationType("TASK_COMMENT")
                            .notificationTarget(taskCommentDto.getTaskNo())
                            .notificationUrl("/projects/" + projectNo + "/kanban?taskNo=" + taskCommentDto.getTaskNo())
                            .notificationContent("작성하신 '" + task.getTaskTitle() + "' 업무에 새 댓글이 등록되었습니다.")
                            .build());
                    }
                }
            }
        }

        return generatedCommentNo;
    }

    @Operation(summary = "댓글 수정")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    @PutMapping(value = "/", produces = "application/json")
    public boolean update(
            @RequestBody TaskCommentDto taskCommentDto,
            @RequestParam(required = false, defaultValue = "0") int projectNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 1;
        boolean result = taskCommentService.update(taskCommentDto);

        if (result && projectNo > 0) {
            simpMessagingTemplate.convertAndSend(
                "/public/projects/" + projectNo + "/kanban",
                Map.of(
                    "eventType", "COMMENT_UPDATED",
                    "projectNo", projectNo,
                    "taskNo", taskCommentDto.getTaskNo(),
                    "commentNo", taskCommentDto.getTaskCommentNo(),
                    "senderEmpNo", loginEmpNo
                )
            );
        }

        return result;
    }

    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    @DeleteMapping(value = "/{taskCommentNo}", produces = "application/json")
    public boolean delete(
            @PathVariable int taskCommentNo,
            @RequestParam(required = false, defaultValue = "0") int projectNo,
            @RequestParam(required = false, defaultValue = "0") int taskNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 1;
        boolean result = taskCommentService.delete(taskCommentNo);

        if (result && projectNo > 0) {
            simpMessagingTemplate.convertAndSend(
                "/public/projects/" + projectNo + "/kanban",
                Map.of(
                    "eventType", "COMMENT_DELETED",
                    "projectNo", projectNo,
                    "taskNo", taskNo,
                    "commentNo", taskCommentNo,
                    "senderEmpNo", loginEmpNo
                )
            );
        }

        return result;
    }
}