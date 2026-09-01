package com.kh.finalprj.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.service.TaskCommentService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "업무 댓글 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/task/comment") // 👈 반드시 /api/task/comment 로 선언
public class TaskCommentRestController {

    @Autowired
    private TaskCommentService taskCommentService;

    @GetMapping("/list/{taskNo}")
    public List<TaskCommentDto> list(@PathVariable int taskNo) {
        return taskCommentService.findComments(taskNo);
    }

    @PostMapping({"", "/"})
    public int add(
            @RequestBody TaskCommentDto taskCommentDto,
            @CurrentUser TokenParseResponseVO parseVO) {
        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 1;
        return taskCommentService.add(taskCommentDto, loginEmpNo);
    }

    @PutMapping({"", "/"})
    public boolean update(@RequestBody TaskCommentDto taskCommentDto) {
        return taskCommentService.update(taskCommentDto);
    }

    @DeleteMapping("/{taskCommentNo}")
    public boolean delete(@PathVariable int taskCommentNo) {
        return taskCommentService.delete(taskCommentNo);
    }
}