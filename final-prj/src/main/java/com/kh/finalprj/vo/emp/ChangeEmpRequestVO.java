package com.kh.finalprj.vo.emp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="본인 정보 변경 정보")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChangeEmpRequestVO {
	
	
	private String prevEmpPassword;
	
	private String newEmpPassword1;
	
	private String newEmpPassword2;
	
	private String empBirth;
	
	private String empContact;
	
	private String empPost;
	
	private String empAddress1;
	
	private String empAddress2;
	
//	private MultipartFile empProfile;
	
	
	
	
	

}
