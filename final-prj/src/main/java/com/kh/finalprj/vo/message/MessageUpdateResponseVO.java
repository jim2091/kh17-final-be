package com.kh.finalprj.vo.message;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//메세지 수정 결과를 서버가 사용자에게 보내는 데이터
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageUpdateResponseVO {
	private int messageNo;//수정된 메세지 번호
	private int channelNo;//어느 채널인지
	private String content;//수정된 내용
	private Timestamp utime;//수정된 시간
}
