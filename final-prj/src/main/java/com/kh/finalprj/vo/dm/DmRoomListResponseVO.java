package com.kh.finalprj.vo.dm;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmRoomListResponseVO {

	private int roomNo;

	//상대방 정보
	private int targetEmpNo;
	private String targetEmpName;
	private String targetPositionName;
	private String targetPresence;
	private Integer targetAttachNo;

	//마지막 메세지
	private String lastMessage;
	private Timestamp lastMessageTime;

}