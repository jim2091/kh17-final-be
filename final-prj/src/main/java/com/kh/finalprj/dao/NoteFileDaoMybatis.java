package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.NoteCommentFileDto;
import com.kh.finalprj.dto.NoteFileDto;
import com.kh.finalprj.vo.note.NoteFileResponseVO;

@Repository
public class NoteFileDaoMybatis implements NoteFileDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public void addNoteFile(NoteFileDto noteFileDto) {
		sqlSession.insert("mapper.notefile.addNoteFile", noteFileDto);
	}

	@Override
	public List<NoteFileResponseVO> selectFileByNoteNo(int noteNo) {
		return sqlSession.selectList("mapper.notefile.selectFilesByNoteNo", noteNo);
	}

	@Override
	public boolean deleteNoteFile(int noteNo, int attachNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("noteNo", noteNo);
		params.put("attachNo", attachNo);
		return sqlSession.delete("mapper.notefile.deleteNoteFile", params) > 0;
	}

	@Override
	public void addCommentFile(NoteCommentFileDto noteCommentFileDto) {
		sqlSession.insert("mapper.notefile.addCommentFile", noteCommentFileDto);
	}

	@Override
	public List<NoteFileResponseVO> selectFileByCommentNo(int noteCommentNo) {
		return sqlSession.selectList("mapper.notefile.selectFileByCommentNo", noteCommentNo);
	}

	@Override
	public boolean deleteCommentFile(int noteCommentNo, int attachNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("noteCommentNo", noteCommentNo);
		params.put("attachNo", attachNo);
		return sqlSession.delete("mapper.notefile.deleteCommentFile", params) > 0;
	}

}
