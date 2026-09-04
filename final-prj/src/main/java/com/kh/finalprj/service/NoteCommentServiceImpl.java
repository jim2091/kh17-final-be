package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.NoteCommentDao;
import com.kh.finalprj.dao.NoteDao;
import com.kh.finalprj.dao.NoteFileDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.NoteCommentDto;
import com.kh.finalprj.vo.note.NoteCommentDetailResponseVO;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.note.NoteFileResponseVO;

@Service
public class NoteCommentServiceImpl implements NoteCommentService {

    @Autowired
    private NoteCommentDao noteCommentDao;

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private ProjectMemberDao projectMemberDao;

    @Autowired(required = false)
    private NoteFileDao noteFileDao;

    @Autowired(required = false)
    private AttachDao attachDao;

    @Autowired(required = false)
    private AttachService attachService;

    // 1. 댓글 등록 (사번 기반 프로젝트 멤버 번호 자동 보정)
    @Override
    @Transactional
    public int add(NoteCommentDto noteCommentDto, int loginEmpNo) {
        NoteDetailResponseVO note = noteDao.selectOne(noteCommentDto.getNoteNo());
        if (note == null) {
            throw new IllegalArgumentException("존재하지 않는 노트입니다.");
        }

        // 로그인 사번으로 project_member_no 조회 및 세팅 (ORA-02291 방어)
        Integer projectMemberNo = projectMemberDao.findProjectMemberNo(note.getProjectNo(), loginEmpNo);
        if (projectMemberNo == null) {
            throw new IllegalStateException("해당 프로젝트에 참여 중인 멤버가 아닙니다.");
        }
        noteCommentDto.setProjectMemberNo(projectMemberNo);

        int generatedCommentNo = noteCommentDao.sequence();
        noteCommentDto.setNoteCommentNo(generatedCommentNo);
        noteCommentDao.add(noteCommentDto);

        return generatedCommentNo;
    }

    // 2. 댓글 단건 상세 조회
    @Override
    @Transactional(readOnly = true)
    public NoteCommentDetailResponseVO selectOne(int noteCommentNo) {
        return noteCommentDao.selectOne(noteCommentNo);
    }

    // 3. 특정 노트의 댓글 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<NoteCommentDto> findComments(int noteNo) {
        return noteCommentDao.findComments(noteNo);
    }

    // 4. 댓글 내용 수정
    @Override
    @Transactional
    public boolean update(NoteCommentDto noteCommentDto) {
        return noteCommentDao.update(noteCommentDto);
    }

    // 5. 댓글 삭제 (외래키 제약조건 방어를 위한 매핑 및 물리 첨부파일 선삭제)
    @Override
    @Transactional
    public boolean delete(int noteCommentNo) {
        if (noteFileDao != null) {
            List<NoteFileResponseVO> files = noteFileDao.selectFileByCommentNo(noteCommentNo);
            if (files != null && !files.isEmpty()) {
                for (NoteFileResponseVO file : files) {
                    noteFileDao.deleteCommentFile(noteCommentNo, file.getAttachNo());

                    if (attachDao != null && attachService != null) {
                        AttachDto attachDto = attachDao.selectOne(file.getAttachNo());
                        String uploader = (attachDto != null && attachDto.getAttachUploader() != null)
                                ? attachDto.getAttachUploader()
                                : "SYSTEM";
                        attachService.delete(file.getAttachNo(), uploader);
                    }
                }
            }
        }

        return noteCommentDao.delete(noteCommentNo);
    }
}