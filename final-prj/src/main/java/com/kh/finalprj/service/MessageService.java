package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.MessageDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.message.ChannelMessageRequestVO;
import com.kh.finalprj.vo.message.ChannelMessageResponseVO;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.message.MessageUpdateRequestVO;
import com.kh.finalprj.vo.message.MessageVO;

@Service
public class MessageService {
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;

	//메세지 등록
	@Transactional
	public MessageVO add(MessageVO message) {
		//(1) 메세지 번호 생성
		int no = messageDao.sequence();
		
		//(2) 생성된 번호 저장
		message.setNo(no);
		
		//(3) DB 등록
		messageDao.add(message);
		
		return message;
	}
	
	//과거 메세지 목록 조회
	public ChannelMessageResponseVO selectList(
			int channelNo, 
			int empNo,
			ChannelMessageRequestVO request
		) {
		
		//(1) 채널(프로젝트)가 존재하는지 확인
		Integer projectNo = channelDao.findProjectNo(channelNo);
		if(projectNo == null) {
			throw new TargetNotfoundException();
		}
		
		//(2) 사용자가 프로젝트 멤버인지 확인
		Integer projectMemberNo = projectMemberDao
					.findProjectMemberNo(projectNo, empNo);
		if(projectMemberNo == null) {
			throw new GetOutException();
		}
		
	    //(3) 메시지 조회
		List<MessageVO> messages = 
				messageDao.selectList(channelNo, request);
		
		//(4) 마지막 메시지 여부 확인
		int count = messageDao.count(channelNo, request);
		boolean last = messages.size() >= count;
		
		//(5) 응답 생성
		return ChannelMessageResponseVO.builder()
					.messages(messages)
					.last(last)
				.build();
	}
	
	//메세지 삭제
	public void delete(int chatMessageNo, int empNo) {
		//(1) 삭제 대상 메세지 조회
		MessageTargetVO target = messageDao.selectTarget(chatMessageNo);
		
		//(2) 메세지가 존재하는지 확인
		if(target == null) {
			throw new TargetNotfoundException();
		}
		
		//(3) 현재 사용자의 projectMemberNo 조회
		Integer projectMemberNo = 
			projectMemberDao.findProjectMemberNo(
				target.getProjectNo(), empNo);
		
		//(4) 프로젝트 참여자인지 확인
		if(projectMemberNo == null) {
			throw new GetOutException();
		}
		
		//(5) 작성자 projectMemberNo와 현재 사용자 비교
		if(target.getProjectMemberNo() != projectMemberNo) {
			throw new GetOutException();
		}
		
		//(6) 메세지 삭제 (soft delete)
		messageDao.delete(chatMessageNo);
	}
	
	//메세지 수정
	public void update(
			int chatMessageNo,
			MessageUpdateRequestVO request, 
			int empNo
		) {
		//(1) 수정 대상 메세지 조회
		MessageTargetVO target = messageDao.selectTarget(chatMessageNo);
		
		//(2) 메세지가 존재하는지 확인
		if(target == null) {
			throw new TargetNotfoundException();
		}
		
		//(3) 현재 사용자의 projectMemberNo 조회
		Integer projectMemberNo = 
			projectMemberDao.findProjectMemberNo(
				target.getProjectNo(), empNo);
		
		//(4) 프로젝트 참여자인지 확인
		if(projectMemberNo == null) {
			throw new GetOutException();
		}
		
		//(5) 작성자 projectMemberNo와 현재 사용자 비교
		if(target.getProjectMemberNo() != projectMemberNo) {
			throw new GetOutException();
		}
		
		//(6) 메세지 수정
		messageDao.update(chatMessageNo, request.getContent());
	}
}
