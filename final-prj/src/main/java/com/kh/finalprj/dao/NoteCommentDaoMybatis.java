package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.NoteCommentDto;
import com.kh.finalprj.vo.note.NoteCommentDetailResponseVO;

@Repository
public class NoteCommentDaoMybatis implements NoteCommentDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int sequence() {
        return sqlSession.selectOne("mapper.notecomment.sequence");
    }

    @Override
    public int add(NoteCommentDto noteCommentDto) {
        return sqlSession.insert("mapper.notecomment.add", noteCommentDto);
    }

    @Override
    public NoteCommentDetailResponseVO selectOne(int noteCommentNo) {
        return sqlSession.selectOne("mapper.notecomment.selectOne", noteCommentNo);
    }

    @Override
    public List<NoteCommentDto> findComments(int noteNo) {
        return sqlSession.selectList("mapper.notecomment.findComments", noteNo);
    }

    @Override
    public boolean update(NoteCommentDto noteCommentDto) {
        return sqlSession.update("mapper.notecomment.update", noteCommentDto) > 0;
    }

    @Override
    public boolean delete(int noteCommentNo) {
        return sqlSession.delete("mapper.notecomment.delete", noteCommentNo) > 0;
    }
}
