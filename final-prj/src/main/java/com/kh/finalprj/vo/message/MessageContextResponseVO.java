package com.kh.finalprj.vo.message;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "특정 메세지 주변 대화 조회 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageContextResponseVO {
	private int channelNo;
	private int targetMessageNo;
	private List<MessageVO> messages;
}
