package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;

@Repository
public class TaskCommentsDaoMybatis implements TaskCommentsDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.taskcomment.sequence");
	}

	@Override
	public int add(TaskCommentDto taskCommentDto) {
		return sqlSession.insert("mapper.taskcomment.add", taskCommentDto);
	}

	@Override
	public TaskCommentDetailResponseVO selectOne(int taskCommentNo) {
		return sqlSession.selectOne("mapper.taskcomment.selectOne", taskCommentNo);
	}

	@Override
	public List<TaskCommentDto> findComments(int taskNo) {
		return sqlSession.selectList("mapper.taskcomment.findComment", taskNo);
	}

	@Override
	public boolean update(TaskCommentDto taskCommentDto) {
		return sqlSession.update("mapper.taskcomment.update", taskCommentDto) > 0;
	}

	@Override
	public boolean delete(int taskCommentNo) {
		return sqlSession.delete("mapper.taskcomment.delete", taskCommentNo) > 0;
	}
}
