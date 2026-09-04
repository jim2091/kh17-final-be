package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.NoteDto;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.note.NoteListRequestVO;

@Repository
public class NoteDaoMybatis implements NoteDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int sequence() {
        return sqlSession.selectOne("mapper.note.sequence");
    }

    @Override
    public void insert(NoteDto noteDto) {
        sqlSession.insert("mapper.note.insert", noteDto);
    }

    @Override
    public NoteDetailResponseVO selectOne(int noteNo) {
        return sqlSession.selectOne("mapper.note.selectOne", noteNo);
    }

    @Override
    public List<NoteDto> selectList(NoteListRequestVO request) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", request.getProjectNo());
        params.put("req", request);
        return sqlSession.selectList("mapper.note.selectList", params);
    }

    @Override
    public int count(NoteListRequestVO request) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", request.getProjectNo());
        params.put("req", request);
        return sqlSession.selectOne("mapper.note.count", params);
    }

    @Override
    public boolean update(NoteDto noteDto) {
        return sqlSession.update("mapper.note.update", noteDto) > 0;
    }

    @Override
    public boolean delete(int noteNo) {
        return sqlSession.delete("mapper.note.delete", noteNo) > 0;
    }
}