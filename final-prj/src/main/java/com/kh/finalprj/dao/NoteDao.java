package com.kh.finalprj.dao;

import java.util.List;
import com.kh.finalprj.dto.NoteDto;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.note.NoteListRequestVO;

public interface NoteDao {
    int sequence();
    void insert(NoteDto noteDto);
    NoteDetailResponseVO selectOne(int noteNo);
    List<NoteDto> selectList(NoteListRequestVO request);
    int count(NoteListRequestVO request);
    boolean update(NoteDto noteDto);
    boolean delete(int noteNo);
}