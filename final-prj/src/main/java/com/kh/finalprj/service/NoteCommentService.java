package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.NoteCommentDto;
import com.kh.finalprj.vo.note.NoteCommentDetailResponseVO;

public interface NoteCommentService {
	int add(NoteCommentDto noteCommentDto, int loginEmpNo);
	NoteCommentDetailResponseVO selectOne(int noteCommentNo);
	List<NoteCommentDto> findComments(int noteNo);
	boolean update(NoteCommentDto noteCommentDto);
	boolean delete(int noteCommentNo);
}
