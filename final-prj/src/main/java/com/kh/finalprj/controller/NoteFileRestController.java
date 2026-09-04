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
import com.kh.finalprj.service.NoteFileService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.note.NoteFileResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "노트 및 댓글 첨부파일 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/note/file")
public class NoteFileRestController {

    @Autowired
    private NoteFileService noteFileService;

    // 1. 노트 본체 첨부파일 업로드
    @Operation(summary = "노트 본체 첨부파일 업로드")
    @ApiResponse(responseCode = "200", description = "파일 업로드 성공")
    @PostMapping(value = "/{noteNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NoteFileResponseVO uploadNoteFile(
            @PathVariable int noteNo,
            @RequestParam(defaultValue = "0") int projectNo,
            @RequestPart("file") MultipartFile file,
            @CurrentUser TokenParseResponseVO parseVO) throws IOException {
        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return noteFileService.uploadNoteFile(noteNo, projectNo, uploader, file);
    }

    // 2. 노트 본체 첨부파일 목록 조회 (/{noteNo} 및 /list/{noteNo} 동시 지원)
    @Operation(summary = "노트 본체 첨부파일 목록 조회")
    @ApiResponse(responseCode = "200", description = "목록 조회 성공")
    @GetMapping(value = {"/{noteNo}", "/list/{noteNo}"})
    public List<NoteFileResponseVO> getNoteFiles(@PathVariable int noteNo) {
        return noteFileService.getNoteFies(noteNo);
    }

    // 3. 노트 본체 첨부파일 개별 삭제
    @Operation(summary = "노트 본체 첨부파일 개별 삭제")
    @ApiResponse(responseCode = "200", description = "파일 삭제 성공")
    @DeleteMapping("/{noteNo}/{attachNo}")
    public boolean removeNoteFile(
            @PathVariable int noteNo,
            @PathVariable int attachNo,
            @CurrentUser TokenParseResponseVO parseVO) {
        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return noteFileService.removeNoteFile(noteNo, attachNo, uploader);
    }

    // 4. 댓글 첨부파일 업로드
    @Operation(summary = "댓글 첨부파일 업로드")
    @ApiResponse(responseCode = "200", description = "댓글 파일 업로드 성공")
    @PostMapping(value = "/comment/{noteCommentNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NoteFileResponseVO uploadCommentFile(
            @PathVariable int noteCommentNo,
            @RequestParam(defaultValue = "0") int projectNo,
            @RequestPart("file") MultipartFile file,
            @CurrentUser TokenParseResponseVO parseVO) throws IOException {
        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return noteFileService.uploadCommentFile(noteCommentNo, projectNo, uploader, file);
    }

    // 5. 댓글 첨부파일 목록 조회 (/comment/{noteCommentNo} 및 /comment/list/{noteCommentNo} 동시 지원)
    @Operation(summary = "댓글 첨부파일 목록 조회")
    @ApiResponse(responseCode = "200", description = "댓글 파일 목록 조회 성공")
    @GetMapping(value = {"/comment/{noteCommentNo}", "/comment/list/{noteCommentNo}"})
    public List<NoteFileResponseVO> getCommentFiles(@PathVariable int noteCommentNo) {
        return noteFileService.getCommentFiles(noteCommentNo);
    }

    // 6. 댓글 첨부파일 개별 삭제
    @Operation(summary = "댓글 첨부파일 개별 삭제")
    @ApiResponse(responseCode = "200", description = "댓글 파일 삭제 성공")
    @DeleteMapping("/comment/{noteCommentNo}/{attachNo}")
    public boolean removeCommentFile(
            @PathVariable int noteCommentNo,
            @PathVariable int attachNo,
            @CurrentUser TokenParseResponseVO parseVO) {
        String uploader = (parseVO != null) ? String.valueOf(parseVO.getEmpNo()) : "SYSTEM";
        return noteFileService.removeCommentFile(noteCommentNo, attachNo, uploader);
    }
}