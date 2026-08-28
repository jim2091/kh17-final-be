package com.kh.finalprj.vo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageUnreadChannelVO {
	private int channelNo;//어느 채널인가
	private int unreadCount;//내가 안 읽은 메세지가 몇 개인가
}
