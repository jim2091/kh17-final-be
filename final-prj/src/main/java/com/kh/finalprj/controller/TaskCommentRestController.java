package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.service.TaskCommentService;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "업무 댓글 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/task/comment")
public class TaskCommentRestController {

	@Autowired
	private TaskCommentService taskCommentService;

	// 1. [C] 댓글 등록 (POST /api/task/comment/)
	@PostMapping("/")
	public int add(@RequestBody TaskCommentDto taskCommentDto) {
		return taskCommentService.add(taskCommentDto);
	}

	// 2. [R] 특정 업무의 댓글 목록 조회 (GET /api/task/comment/list/{taskNo})
	@GetMapping("/list/{taskNo}")
	public List<TaskCommentDto> list(@PathVariable int taskNo) {
		return taskCommentService.findComments(taskNo);
	}

	// 3. [R] 단건 댓글 상세 조회 (GET /api/task/comment/{taskCommentNo})
	@GetMapping("/{taskCommentNo}")
	public TaskCommentDetailResponseVO selectOne(@PathVariable int taskCommentNo) {
		return taskCommentService.selectOne(taskCommentNo);
	}

	// 4. [U] 댓글 수정 (PUT /api/task/comment/)
	@PutMapping("/")
	public boolean update(@RequestBody TaskCommentDto taskCommentDto) {
		return taskCommentService.update(taskCommentDto);
	}

	// 5. [D] 댓글 삭제 (DELETE /api/task/comment/{taskCommentNo})
	@DeleteMapping("/{taskCommentNo}")
	public boolean delete(@PathVariable int taskCommentNo) {
		return taskCommentService.delete(taskCommentNo);
	}
}