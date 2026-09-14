package com.kh.finalprj.vo.record;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(name = "프로젝트 record 목록 조회 요청 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRecordListRequestVO {
	
	//전체 / 의사결정 / 이슈 / 산출물 / 기타
	@Pattern(regexp = "^(ALL|DECISION|ISSUE|DELIVERABLE|ETC)$")
	private String type = "ALL";
	
	//제목, 내용, 관련 원본 검색
	private String keyword = "";
	
	//ISSUE 상태
	@Pattern(regexp = "^(ALL|OPEN|RESOLVED)$")
	private String issueStatus = "ALL";
	
	//연결된 원본 유형
	@Pattern(regexp = "^(ALL|TASK|MESSAGE|NOTE|ATTACH)$")
	private String relatedType = "ALL";
	
	//작성자 projectMemberNo
	private Integer writerNo;
	
	//작성일 범위
	private LocalDate startDate;
	private LocalDate endDate;
	
	//최신 작성순 / 오래된 작성순 / 최근 수정순
	@Pattern(regexp = "^(LATEST|OLDEST|UPDATED)$")
	private String sort = "LATEST";
	
	//더보기
	private int page = 0;
	private int size = 10;
	
	public int getBeginRownum() {
		return page * size - (size - 1);
	}
	
	public int getEndRownum() {
		return page * size;
	}
}
