package com.kh.finalprj.dao;

import java.util.List;
import com.kh.finalprj.dto.NoteCommentDto;
import com.kh.finalprj.vo.note.NoteCommentDetailResponseVO;

public interface NoteCommentDao {
    int sequence();
    int add(NoteCommentDto noteCommentDto);
    NoteCommentDetailResponseVO selectOne(int noteCommentNo);
    List<NoteCommentDto> findComments(int noteNo);
    boolean update(NoteCommentDto noteCommentDto);
    boolean delete(int noteCommentNo);
}