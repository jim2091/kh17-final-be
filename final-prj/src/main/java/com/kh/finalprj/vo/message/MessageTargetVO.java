package com.kh.finalprj.vo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//특정 메세지에 대한 작업(삭제 · 수정 · 권한 확인 등)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageTargetVO {
	private int chatMessageNo;
	private int channelNo;
	private int projectNo;
	private int projectMemberNo;
}
