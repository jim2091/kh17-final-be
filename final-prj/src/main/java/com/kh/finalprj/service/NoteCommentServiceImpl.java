package com.kh.finalprj.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.NoteCommentDao;
import com.kh.finalprj.dao.NoteDao;
import com.kh.finalprj.dao.NoteFileDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
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

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired(required = false)
    private NoteFileDao noteFileDao;

    @Autowired(required = false)
    private AttachDao attachDao;

    @Autowired(required = false)
    private AttachService attachService;

    // 1. 댓글 등록 및 실시간 알림/동기화 발송
    @Override
    @Transactional
    public int add(NoteCommentDto noteCommentDto, int loginEmpNo) {
        NoteDetailResponseVO note = noteDao.selectOne(noteCommentDto.getNoteNo());
        if (note == null) {
            throw new IllegalArgumentException("존재하지 않는 노트입니다.");
        }

        Integer projectMemberNo = projectMemberDao.findProjectMemberNo(note.getProjectNo(), loginEmpNo);
        if (projectMemberNo == null) {
            throw new IllegalStateException("해당 프로젝트에 참여 중인 멤버가 아닙니다.");
        }
        noteCommentDto.setProjectMemberNo(projectMemberNo);

        int generatedCommentNo = noteCommentDao.sequence();
        noteCommentDto.setNoteCommentNo(generatedCommentNo);
        noteCommentDao.add(noteCommentDto);

        if (generatedCommentNo > 0) {
            int projectNo = note.getProjectNo();
            String targetUrl = "/projects/" + projectNo + "/notes?noteNo=" + note.getNoteNo();
            String notiContent = "'" + note.getNoteTitle() + "' 노트에 새 댓글이 등록되었습니다.";

            // 1) 실시간 노트 댓글창 동기화 브로드캐스트 전송
            simpMessagingTemplate.convertAndSend(
                "/public/projects/" + projectNo + "/notes",
                Map.of(
                    "eventType", "NOTE_COMMENT_ADDED",
                    "projectNo", projectNo,
                    "noteNo", note.getNoteNo(),
                    "commentNo", generatedCommentNo,
                    "senderEmpNo", loginEmpNo
                )
            );

            // 2) 중복 방지 개인 알림 전송 (Set 활용)
            Set<Integer> receiverEmpNos = new HashSet<>();
            if (note.getNoteWriterNo() > 0) {
                ProjectMemberDto writerMember = projectMemberDao.findMember(note.getNoteWriterNo());
                if (writerMember != null && writerMember.getEmpNo() != loginEmpNo) {
                    receiverEmpNos.add(writerMember.getEmpNo());
                }
            }

            for (int receiverEmpNo : receiverEmpNos) {
                notificationService.send(NotificationDto.builder()
                        .notificationReceiver(receiverEmpNo)
                        .projectNo(projectNo)
                        .notificationType("NOTE_COMMENT")
                        .notificationTarget(note.getNoteNo())
                        .notificationUrl(targetUrl)
                        .notificationContent(notiContent)
                        .build());
            }
        }

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

    // 4. 댓글 내용 수정 및 실시간 동기화 브로드캐스트
    @Override
    @Transactional
    public boolean update(NoteCommentDto noteCommentDto) {
        boolean result = noteCommentDao.update(noteCommentDto);

        if (result) {
            NoteCommentDetailResponseVO comment = noteCommentDao.selectOne(noteCommentDto.getNoteCommentNo());
            if (comment != null) {
                NoteDetailResponseVO note = noteDao.selectOne(comment.getNoteNo());
                if (note != null) {
                    simpMessagingTemplate.convertAndSend(
                        "/public/projects/" + note.getProjectNo() + "/notes",
                        Map.of(
                            "eventType", "NOTE_COMMENT_UPDATED",
                            "projectNo", note.getProjectNo(),
                            "noteNo", comment.getNoteNo(),
                            "commentNo", noteCommentDto.getNoteCommentNo()
                        )
                    );
                }
            }
        }

        return result;
    }

    // 5. 댓글 삭제 및 실시간 동기화 브로드캐스트
    @Override
    @Transactional
    public boolean delete(int noteCommentNo) {
        NoteCommentDetailResponseVO comment = noteCommentDao.selectOne(noteCommentNo);
        NoteDetailResponseVO note = (comment != null) ? noteDao.selectOne(comment.getNoteNo()) : null;

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

        boolean result = noteCommentDao.delete(noteCommentNo);

        if (result && note != null) {
            simpMessagingTemplate.convertAndSend(
                "/public/projects/" + note.getProjectNo() + "/notes",
                Map.of(
                    "eventType", "NOTE_COMMENT_DELETED",
                    "projectNo", note.getProjectNo(),
                    "noteNo", note.getNoteNo(),
                    "commentNo", noteCommentNo
                )
            );
        }

        return result;
    }
}