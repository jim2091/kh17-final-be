package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.vo.note.NoteFileResponseVO;

public interface NoteFileService {

    NoteFileResponseVO uploadNoteFile(int noteNo, int projectNo, String uploader, MultipartFile file) throws IOException;

    List<NoteFileResponseVO> getNoteFies(int noteNo);

    boolean removeNoteFile(int noteNo, int attachNo, String uploader);

    NoteFileResponseVO uploadCommentFile(int noteCommentNo, int projectNo, String uploader, MultipartFile file) throws IOException;

    List<NoteFileResponseVO> getCommentFiles(int noteCommentNo);

    boolean removeCommentFile(int noteCommentNo, int attachNo, String uploader);
}