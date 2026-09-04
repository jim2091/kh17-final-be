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
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dto.NoteCommentDto;
import com.kh.finalprj.service.NoteCommentService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.note.NoteCommentDetailResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "노트 댓글 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/note/comment")
public class NoteCommentRestController {

    @Autowired
    private NoteCommentService noteCommentService;

    // 1. 특정 노트의 댓글 목록 조회
    @Operation(summary = "노트별 댓글 목록 조회")
    @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")
    @GetMapping("/list/{noteNo}")
    public List<NoteCommentDto> list(@PathVariable int noteNo) {
        return noteCommentService.findComments(noteNo);
    }

    // 2. 댓글 단건 상세 조회
    @Operation(summary = "댓글 단건 상세 조회")
    @ApiResponse(responseCode = "200", description = "댓글 상세 조회 성공")
    @GetMapping("/{noteCommentNo}")
    public NoteCommentDetailResponseVO detail(@PathVariable int noteCommentNo) {
        return noteCommentService.selectOne(noteCommentNo);
    }

    // 3. 신규 댓글 등록
    @Operation(summary = "신규 댓글 등록")
    @ApiResponse(responseCode = "200", description = "댓글 등록 성공")
    @PostMapping("/")
    public int add(
            @Valid @RequestBody NoteCommentDto noteCommentDto,
            @CurrentUser TokenParseResponseVO parseVO) {
        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
        return noteCommentService.add(noteCommentDto, loginEmpNo);
    }

    // 4. 댓글 내용 수정
    @Operation(summary = "댓글 내용 수정")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    @PutMapping("/")
    public boolean update(@Valid @RequestBody NoteCommentDto noteCommentDto) {
        return noteCommentService.update(noteCommentDto);
    }

    // 5. 댓글 삭제
    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    @DeleteMapping("/{noteCommentNo}")
    public boolean delete(@PathVariable int noteCommentNo) {
        return noteCommentService.delete(noteCommentNo);
    }
}