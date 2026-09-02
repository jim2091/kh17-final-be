package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
@Repository
public class ProjectDaoMybatis implements ProjectDao{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.project.sequence");
	}

	@Override
	public void add(ProjectDto projectDto) {
		sqlSession.insert("mapper.project.add",projectDto);
	}

	@Override
	public List<ProjectListResponseVO> selectMyProjectList(int empNo) {
		return sqlSession.selectList("mapper.project.myProjectList",empNo);
	}

	@Override
	public ProjectDetailResponseVO selectOne(int projectNo, int empNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("empNo", empNo);
		return sqlSession.selectOne("mapper.project.detail",params);
	}

	@Override
	public boolean update(ProjectDto projectDto) {
		return sqlSession.update("mapper.project.update",projectDto)>0;
	}

	@Override
	public int countPublicProject(PageVO pageVO) {
		return sqlSession.selectOne("mapper.project.countPublicProject",pageVO);
	}

	@Override
	public List<ProjectListResponseVO> selectPublicProjectList(PageVO pageVO, int empNo) {
		Map<String,Object>params = new HashMap<>();
		params.put("pageVO",pageVO);
		params.put("empNo", empNo);
		return sqlSession.selectList("mapper.project.publicProjectList",params);
	}

	@Override
	public ProjectDto selectProject(int projectNo) {
		return sqlSession.selectOne("mapper.project.selectProject",projectNo);
	}

	@Override
	public int delete(int projectNo) {
		return sqlSession.delete("mapper.project.delete",projectNo);
	}

}
