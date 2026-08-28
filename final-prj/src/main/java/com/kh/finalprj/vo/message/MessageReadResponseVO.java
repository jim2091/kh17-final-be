package com.kh.finalprj.vo.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//사용자가 메세지를 읽었을 때 서버가 보내는 데이터
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageReadResponseVO {
	private int channelNo;//어느 채널에서
	private int projectMemberNo;//누가 읽었는지
	private List<MessageUnreadVO> messages;//메시지별 unreadCount를 담을 수 있는 구조
}
