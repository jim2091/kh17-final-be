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
import com.kh.finalprj.vo.message.MessageReadResponseVO;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.message.MessageUnreadChannelVO;
import com.kh.finalprj.vo.message.MessageUnreadVO;
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
		message.setNo(no);
		
		//(3) 메세지 등록
		messageDao.add(message);
		
		//(4) DB에 저장된 메세지를 다시 조회
		return messageDao.selectOne(no);
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
	public MessageTargetVO delete(int chatMessageNo, int empNo) {
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
		
		//(7) 삭제된 메세지의 기본 정보 반환
		return target;
	}
	
	
	//메세지 수정
	public MessageVO update(
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
		
		//(7) 수정된 메세지 다시 조회
		return messageDao.selectOne(chatMessageNo);
	}
	
	
	//해당 채널의 안 읽은 메시지를 한꺼번에 읽음 처리
	@Transactional
	public MessageReadResponseVO readChannelMessage(int channelNo, int empNo) {
		//(1) 채널이 속한 프로젝트 번호 조회
		int projectNo = channelDao.findProjectNo(channelNo);
		
		//(2) 현재 사용자의 projectMemberNo 조회
		Integer projectMemberNo = 
			projectMemberDao.findProjectMemberNo(
				projectNo, empNo);
		
		//(3) 프로젝트 참여자인지 확인
		if(projectMemberNo == null) {
			throw new GetOutException();
		}
		
		//(4) 현재 사용자가 채널의 메시지를 모두 읽음 처리
		messageDao.readChannelMessage(channelNo, projectMemberNo);
		
		//(5) 읽음 처리 후 메세지별 unreadCount 조회
		List<MessageUnreadVO> messages = messageDao.selectUnreadCount(channelNo);
		
		//(6) Websocket으로 보낼 응답 생성
		return MessageReadResponseVO.builder()
					.channelNo(channelNo)
					.projectMemberNo(projectMemberNo)
					.messages(messages)
				.build();
	}
	
	
	//특정 메세지의 안 읽은 사람 수
	public int countUnread(
			int chatMessageNo, int channelNo, int projectMemberNo
		) {
		return messageDao.countUnread(chatMessageNo, channelNo, projectMemberNo);
	}
	
	//채널별 내가 안 읽은 메세지 수 조회
	public List<MessageUnreadChannelVO> selectChannelUnreadCount(
			int projectNo, int projectMemberNo
		) {
		return messageDao.selectChannelUnreadCount(
				projectNo, projectMemberNo);
	}
}
