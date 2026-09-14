package com.kh.finalprj.vo.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KakaoTokenResponseVO {
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expires_in")
	private Integer expiredIn;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("refresh_token_expires_in")
	private Integer refreshTokenExpiresIn;

}
