package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.NoteCommentFileDto;
import com.kh.finalprj.dto.NoteFileDto;
import com.kh.finalprj.vo.note.NoteFileResponseVO;

public interface NoteFileDao {
	//노트 본체 첨부파일
	void addNoteFile(NoteFileDto noteFileDto);
	List<NoteFileResponseVO> selectFileByNoteNo(int noteNo);
	boolean deleteNoteFile(int noteNo, int attachNo);
	
	//댓글 첨부파일
	void addCommentFile(NoteCommentFileDto noteCommentFileDto);
	List<NoteFileResponseVO>selectFileByCommentNo(int noteCommentNo);
	boolean deleteCommentFile(int noteCommentNo, int attachNo);
}
