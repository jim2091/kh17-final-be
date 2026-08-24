package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChannelDto {
	private int chatChannelNo;
	private int projectNo;
	private Integer chatChannelCreator;//→ project_member_no
	private String chatChannelName;
	private Timestamp chatChannelCtime;
	private Timestamp chatChannelUtime;
	
	//채널 생성자 이름 (조회용)
	private String chatChannelCreatorName;
}
