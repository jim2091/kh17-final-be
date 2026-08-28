package com.kh.finalprj.vo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//메시지별 unreadCount를 담을 수 있는 구조
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageUnreadVO {
	private int messageNo;
	private int unreadCount;
}
