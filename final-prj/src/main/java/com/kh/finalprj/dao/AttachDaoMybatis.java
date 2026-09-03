package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachProfileVO;

@Repository
public class AttachDaoMybatis implements AttachDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.attach.sequence");
	}

	@Override
	public void insert(AttachDto attachDto) {
		sqlSession.insert("mapper.attach.add", attachDto);
	}

	@Override
	public AttachDto selectOne(int attachNo) {
		return sqlSession.selectOne("mapper.attach.find", attachNo);
	}

	@Override
	public AttachDto selectOne(Integer attachNo) {

		if (attachNo == null) {
			return null;
		}

		return sqlSession.selectOne("mapper.attach.find", attachNo);
	}

	@Override
	public boolean delete(int attachNo) {
		return sqlSession.delete("mapper.attach.delete", attachNo) > 0;
	}

	@Override
	public List<AttachDto> selectList(List<Integer> attachNumbers) {

		if (attachNumbers == null || attachNumbers.isEmpty()) {

			return List.of();
		}

		return sqlSession.selectList("mapper.attach.findList", attachNumbers);
	}

	@Override
	public List<AttachDto> selectListByProject(int projectNo) {

		return sqlSession.selectList("mapper.attach.selectListByProject", projectNo);
	}

	@Override
	public List<AttachDto> selectListByProjectAndKeyword(int projectNo, String keyword) {

		return sqlSession.selectList("mapper.attach.selectListByProjectAndKeyword",
				new ProjectFileSearch(projectNo, keyword));
	}

	private static class ProjectFileSearch {

		private final int projectNo;
		private final String keyword;

		public ProjectFileSearch(int projectNo, String keyword) {
			this.projectNo = projectNo;
			this.keyword = keyword;
		}

		public int getProjectNo() {
			return projectNo;
		}

		public String getKeyword() {
			return keyword;
		}
	}
	@Override
	public void insert(AttachProfileVO attachProfileVO) {
		sqlSession.insert("mapper.attach.add", attachProfileVO);		
	}


}