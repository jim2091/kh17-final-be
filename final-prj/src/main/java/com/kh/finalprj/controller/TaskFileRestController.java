package com.kh.finalprj.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.TaskFileService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "업무 및 댓글 첨부파일 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/task/file")
public class TaskFileRestController {

    @Autowired
    private TaskFileService taskFileService;

    // 업무 본체 첨부파일 업로드
    @Operation(summary = "업무 본체 첨부파일 업로드")
    @PostMapping(value = "/{taskNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TaskFileResponseVO uploadTaskFile(
            @PathVariable int taskNo,
            @RequestParam(defaultValue = "0") int projectNo,
            @RequestPart("file") MultipartFile file,
            @CurrentUser TokenParseResponseVO parseVO) throws IOException {

        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return taskFileService.uploadTaskFile(taskNo, projectNo, uploader, file);
    }

    // 업무 본체 첨부파일 목록 조회
    @Operation(summary = "업무 본체 첨부파일 목록 조회")
    @GetMapping(value = "/{taskNo}")
    public List<TaskFileResponseVO> getTaskFiles(@PathVariable int taskNo) {
        return taskFileService.getTaskFiles(taskNo);
    }

    // 업무 본체 첨부파일 삭제
    @Operation(summary = "업무 본체 첨부파일 삭제")
    @DeleteMapping(value = "/{taskNo}/{attachNo}")
    public boolean removeTaskFile(
            @PathVariable int taskNo,
            @PathVariable int attachNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return taskFileService.removeTaskFile(taskNo, attachNo, uploader);
    }

    // 댓글 첨부파일 업로드
    @Operation(summary = "댓글 첨부파일 업로드")
    @PostMapping(value = "/comment/{taskCommentNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TaskFileResponseVO uploadCommentFile(
            @PathVariable int taskCommentNo,
            @RequestParam(defaultValue = "0") int projectNo,
            @RequestPart("file") MultipartFile file,
            @CurrentUser TokenParseResponseVO parseVO) throws IOException {

        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return taskFileService.uploadCommentFile(taskCommentNo, projectNo, uploader, file);
    }

    // 댓글 첨부파일 목록 조회
    @Operation(summary = "댓글 첨부파일 목록 조회")
    @GetMapping(value = "/comment/{taskCommentNo}")
    public List<TaskFileResponseVO> getCommentFiles(@PathVariable int taskCommentNo) {
        return taskFileService.getCommentFiles(taskCommentNo);
    }

    // 댓글 첨부파일 삭제
    @Operation(summary = "댓글 첨부파일 삭제")
    @DeleteMapping(value = "/comment/{taskCommentNo}/{attachNo}")
    public boolean removeCommentFile(
            @PathVariable int taskCommentNo,
            @PathVariable int attachNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return taskFileService.removeCommentFile(taskCommentNo, attachNo, uploader);
    }
}