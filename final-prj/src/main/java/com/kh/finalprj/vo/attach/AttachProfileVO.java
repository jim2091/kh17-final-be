package com.kh.finalprj.vo.attach;




import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="파일정보")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttachProfileVO {
	private int attachNo;
	private String attachName;
	private String attachType;
	private long attachSize;
	
	//파일 유형(MIME TYPE)을 알려주기 위한 메소드
	//-만약 유형을 알 수 없으면 null 대신 application/octet-stream을 반환한다
	public String getAttachTypeString() {
		if(attachType == null) {
			return "application/octet-stream";}
		return attachType;
	}

}
