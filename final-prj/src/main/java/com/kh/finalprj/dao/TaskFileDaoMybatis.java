package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.TaskCommentFileDto;
import com.kh.finalprj.dto.TaskFileDto;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

@Repository
public class TaskFileDaoMybatis implements TaskFileDao {

	@Autowired
	private SqlSession sqlSession;
	
	// 업무 본체 첨부파일 등록
	@Override
	public void addTaskFile(TaskFileDto taskFileDto) {
		sqlSession.insert("mapper.taskfile.addTaskFile", taskFileDto);
	}

	// 업무 본체 첨부파일 목록 조회 (대소문자 일치 완료)
	@Override
	public List<TaskFileResponseVO> selectFilesByTaskNo(int taskNo) {
		return sqlSession.selectList("mapper.taskfile.selectFilesByTaskNo", taskNo);
	}

	// 업무 본체 첨부파일 삭제
	@Override
	public boolean deleteTaskFile(int taskNo, int attachNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("taskNo", taskNo);
		params.put("attachNo", attachNo);
		return sqlSession.delete("mapper.taskfile.deleteTaskFile", params) > 0;
	}
	
	// 댓글 첨부파일 등록
	@Override
	public void addCommentFile(TaskCommentFileDto taskCommentFileDto) {
		sqlSession.insert("mapper.taskfile.addCommentFile", taskCommentFileDto);
	}

	// 댓글 첨부파일 목록 조회
	@Override
	public List<TaskFileResponseVO> selectFilesByCommentNo(int taskCommentNo) {
		return sqlSession.selectList("mapper.taskfile.selectFilesByCommentNo", taskCommentNo);
	}

	// 댓글 첨부파일 삭제
	@Override
	public boolean deleteCommentFile(int taskCommentNo, int attachNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("taskCommentNo", taskCommentNo);
		params.put("attachNo", attachNo);
		return sqlSession.delete("mapper.taskfile.deleteCommentFile", params) > 0;
	}

}