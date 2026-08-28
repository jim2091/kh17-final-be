package com.kh.finalprj.vo.message;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageDeleteResponseVO {
	private int messageNo;//삭제된 메세지 번호
	private int channelNo;//어느 채널인지
	private String deleted;//삭제 여부
}
