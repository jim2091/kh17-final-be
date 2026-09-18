package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.kh.finalprj.dao.NoteDao;
import com.kh.finalprj.dto.NoteCommentDto;
import com.kh.finalprj.dto.NoteDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.NoteCommentService;
import com.kh.finalprj.service.NoteFileService;
import com.kh.finalprj.service.ProjectPermissionService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.note.NoteAddRequestVO;
import com.kh.finalprj.vo.note.NoteAddResponseVO;
import com.kh.finalprj.vo.note.NoteDeleteResponseVO;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.note.NoteEditRequestVO;
import com.kh.finalprj.vo.note.NoteEditResponseVO;
import com.kh.finalprj.vo.note.NoteFileResponseVO;
import com.kh.finalprj.vo.note.NoteListRequestVO;
import com.kh.finalprj.vo.note.NoteListResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "노트 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/note")
public class NoteRestController {

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private ProjectPermissionService projectPermissionService;

    /*
     * 노트 본체 첨부파일 처리
     */
    @Autowired
    private NoteFileService noteFileService;

    /*
     * 노트 댓글 및 댓글 첨부파일 처리
     */
    @Autowired
    private NoteCommentService noteCommentService;


    // 1. 프로젝트별 노트 목록 조회 (무한 스크롤 / 검색)
    @ApiResponse(responseCode = "200", description = "노트 목록 조회 성공")
    @PostMapping("/project/{projectNo}/list")
    public NoteListResponseVO list(
            @PathVariable int projectNo,
            @Valid @RequestBody NoteListRequestVO request,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;

        projectPermissionService.checkReadPermission(
                projectNo,
                loginEmpNo
        );

        request.setProjectNo(projectNo);

        List<NoteDto> noteList =
                noteDao.selectList(request);

        int count =
                noteDao.count(request);

        boolean last =
                noteList.isEmpty()
                || noteList.size() >= count;

        return NoteListResponseVO.builder()
                .noteList(noteList)
                .last(last)
                .build();
    }


    // 2. 신규 노트 등록
    @ApiResponse(responseCode = "200", description = "노트 등록 성공")
    @PostMapping("/project/{projectNo}")
    public NoteAddResponseVO add(
            @PathVariable int projectNo,
            @Valid @RequestBody NoteAddRequestVO request,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo =
                (parseVO != null)
                ? parseVO.getEmpNo()
                : 0;

        int memberNo =
                projectPermissionService.findProjectMemberNo(
                        projectNo,
                        loginEmpNo
                );

        int noteNo =
                noteDao.sequence();

        noteDao.insert(
                NoteDto.builder()
                        .noteNo(noteNo)
                        .projectNo(projectNo)
                        .noteTitle(request.getNoteTitle())
                        .noteContent(request.getNoteContent())
                        .noteWriterNo(memberNo)
                        .build()
        );

        return NoteAddResponseVO.builder()
                .noteNo(noteNo)
                .build();
    }


    // 3. 노트 단건 상세 조회
    @ApiResponse(responseCode = "200", description = "노트 상세 조회 성공")
    @GetMapping("/{noteNo}")
    public NoteDetailResponseVO detail(
            @PathVariable int noteNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo =
                (parseVO != null)
                ? parseVO.getEmpNo()
                : 0;

        NoteDetailResponseVO noteDetail =
                noteDao.selectOne(noteNo);

        if (noteDetail == null) {
            throw new TargetNotfoundException();
        }

        projectPermissionService.checkMember(
                noteDetail.getProjectNo(),
                loginEmpNo
        );

        return noteDetail;
    }


    // 4. 노트 내용 수정 (작성자 본인 검증)
    @ApiResponse(responseCode = "200", description = "노트 수정 성공")
    @PutMapping("/{noteNo}")
    public NoteEditResponseVO edit(
            @PathVariable int noteNo,
            @Valid @RequestBody NoteEditRequestVO request,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo =
                (parseVO != null)
                ? parseVO.getEmpNo()
                : 0;

        NoteDetailResponseVO noteDetail =
                noteDao.selectOne(noteNo);

        if (noteDetail == null) {
            throw new TargetNotfoundException();
        }

        int memberNo =
                projectPermissionService.findProjectMemberNo(
                        noteDetail.getProjectNo(),
                        loginEmpNo
                );

        if (memberNo != noteDetail.getNoteWriterNo()) {
            throw new GetOutException();
        }

        boolean result =
                noteDao.update(
                        NoteDto.builder()
                                .noteNo(noteNo)
                                .noteTitle(request.getNoteTitle())
                                .noteContent(request.getNoteContent())
                                .build()
                );

        if (!result) {
            throw new TargetNotfoundException();
        }

        return NoteEditResponseVO.builder()
                .noteNo(noteNo)
                .build();
    }


    // 5. 노트 삭제
    //
    // 노트를 삭제하면
    // ① 노트 본체 첨부파일 삭제
    // ② 노트 댓글 삭제
    // ③ 댓글 첨부파일 삭제
    // ④ 노트 본체 삭제
    //
    // 순서로 처리한다.
    @ApiResponse(responseCode = "200", description = "노트 삭제 성공")
    @DeleteMapping("/{noteNo}")
    @Transactional
    public NoteDeleteResponseVO delete(
            @PathVariable int noteNo,
            @CurrentUser TokenParseResponseVO parseVO) {

        int loginEmpNo =
                (parseVO != null)
                ? parseVO.getEmpNo()
                : 0;


        // ==========================================
        // 1. 노트 존재 여부 확인
        // ==========================================

        NoteDetailResponseVO noteDetail =
                noteDao.selectOne(noteNo);

        if (noteDetail == null) {
            throw new TargetNotfoundException();
        }


        // ==========================================
        // 2. 삭제 권한 확인
        // ==========================================

        int memberNo =
                projectPermissionService.findProjectMemberNo(
                        noteDetail.getProjectNo(),
                        loginEmpNo
                );

        /*
         * 노트 작성자 본인이 아니면
         * 프로젝트 관리자 또는 소유자인지 확인
         */
        if (memberNo != noteDetail.getNoteWriterNo()) {
            projectPermissionService.checkOwnerOrManager(
                    noteDetail.getProjectNo(),
                    loginEmpNo
            );
        }


        // ==========================================
        // 3. 노트 본체 첨부파일 삭제
        // ==========================================
        //
        // note_file
        //      ↓
        // attach
        //      ↓
        // project_file
        //
        // 연결된 파일을 먼저 정리한다.
        // ==========================================

        List<NoteFileResponseVO> noteFiles =
                noteFileService.getNoteFies(noteNo);

        if (noteFiles != null && !noteFiles.isEmpty()) {

            for (NoteFileResponseVO file : noteFiles) {

                noteFileService.removeNoteFile(
                        noteNo,
                        file.getAttachNo(),
                        "SYSTEM"
                );
            }
        }


        // ==========================================
        // 4. 노트의 댓글 삭제
        // ==========================================
        //
        // NoteCommentServiceImpl.delete() 내부에서
        // 댓글 첨부파일도 함께 처리하고 있기 때문에
        // 기존 댓글 삭제 로직을 그대로 사용한다.
        // ==========================================

        List<NoteCommentDto> comments =
                noteCommentService.findComments(noteNo);

        if (comments != null && !comments.isEmpty()) {

            for (NoteCommentDto comment : comments) {

                noteCommentService.delete(
                        comment.getNoteCommentNo()
                );
            }
        }


        // ==========================================
        // 5. 마지막으로 노트 본체 삭제
        // ==========================================

        boolean result =
                noteDao.delete(noteNo);

        if (!result) {
            throw new TargetNotfoundException();
        }


        // ==========================================
        // 6. 삭제 결과 반환
        // ==========================================

        return NoteDeleteResponseVO.builder()
                .noteNo(noteNo)
                .build();
    }
}
