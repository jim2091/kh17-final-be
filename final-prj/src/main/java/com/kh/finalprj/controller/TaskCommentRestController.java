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
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.service.TaskCommentService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

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

    @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")
    @GetMapping(value = "/list/{taskNo}", produces = "application/json")
    public List<TaskCommentDto> list(@PathVariable int taskNo) {
        return taskCommentService.findComments(taskNo);
    }

    @ApiResponse(responseCode = "200", description = "댓글 등록 성공")
    @PostMapping(value = "/", produces = "application/json")
    public int add(
            @RequestBody TaskCommentDto taskCommentDto,
            @RequestParam(required = false, defaultValue = "0") int projectNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 1; // Swagger 테스트 및 NPE 방어
        int generatedCommentNo = taskCommentService.add(taskCommentDto, loginEmpNo);

        // 댓글 등록 성공 및 프로젝트 번호가 존재할 때 실시간 소켓 브로드캐스트 전파
        if (generatedCommentNo > 0 && projectNo > 0) {
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
        }

        return generatedCommentNo;
    }

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