package com.kh.finalprj.vo.message;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "채널 메세지 검색 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageSearchResponseVO {
	private List<MessageVO> messages;
	private int totalCount;
	private boolean last;
}
