package com.kh.finalprj.vo.message;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//화면에 메시지를 보여주기 위한 기본적인 VO
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageVO {
	private int no;
    private int channelNo;
    private Integer projectMemberNo;
    private int empNo;
    private String senderName;
    private String content;
    private String type;
    private Timestamp ctime;
    private Timestamp utime;
    private String deleted;
    private int unreadCount;//해당 메시지를 아직 읽지 않은 사람 수
}
