package com.kh.finalprj.service;

public interface ProjectInviteService {

	//초대
	void invite(int projectNo,int senderEmpNo,int receiverEmpNo);
	//수락
	void accept(int projectInviteNo,int empNo);
	//거절
	void reject(int projectInviteNo,int empNo);
}
