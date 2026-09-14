package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "초대 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectInviteDto {

	private int projectInviteNo;
	private int projectNo;
	private int projectInviteSender;
	private int projectInviteReceiver;
	private String projectInviteStatus;
	private Timestamp projectInviteCtime;
	private Timestamp projectInviteRtime;
}
