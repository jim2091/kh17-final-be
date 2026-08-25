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
import com.kh.finalprj.vo.channel.ChannelMessageRequestVO;
import com.kh.finalprj.vo.channel.ChannelMessageResponseVO;
import com.kh.finalprj.vo.channel.MessageVO;

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
		//메세지 번호 생성
		int no = messageDao.sequence();
		
		//생성된 번호 저장
		message.setNo(no);
		
		//DB 등록
		messageDao.add(message);
		
		return message;
	}
	
	//메세지 조회
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
}
