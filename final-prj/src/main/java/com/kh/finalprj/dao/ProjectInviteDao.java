package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectInviteDto;

public interface ProjectInviteDao {

	//초대 등록
	int sequence();
	void add(ProjectInviteDto projectInviteDto);
	//초대 상세
	ProjectInviteDto find(int projectInviteNo);
	//초대 확인
	int countPending(int projectNo,int projectInviteReceiver);
	//초대 수락
	int accept(int projectInviteNo);
	//초대 거절
	int reject(int projectInviteNo);
}
