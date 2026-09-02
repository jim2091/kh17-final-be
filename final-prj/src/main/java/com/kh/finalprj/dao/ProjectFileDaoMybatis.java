package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectFileDaoMybatis implements ProjectFileDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public void insert(int projectNo, int attachNo) {

		ProjectFileParam param = new ProjectFileParam(projectNo, attachNo);

		sqlSession.insert("mapper.projectFile.add", param);
	}

	private static class ProjectFileParam {

		private final int projectNo;
		private final int attachNo;

		public ProjectFileParam(int projectNo, int attachNo) {
			this.projectNo = projectNo;
			this.attachNo = attachNo;
		}

		public int getProjectNo() {
			return projectNo;
		}

		public int getAttachNo() {
			return attachNo;
		}
	}
}