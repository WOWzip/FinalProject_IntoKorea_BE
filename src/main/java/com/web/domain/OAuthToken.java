package com.web.domain;

import lombok.Data;

/** 토큰 정보 **/
// Token을 json형식으로 변환 -> json형식의 키값과 변수명이 일치해야함.
@Data
public class OAuthToken {

	private String access_token;
	private String token_type;
	private String refresh_token;
	private String id_token;
	private int expires_in;	// 액세스 토큰 만료 시간(초)
	private String scope;
	private int refresh_token_expires_in;	//토큰 발급 시 응답으로 받은 refresh_token // Access Token을 갱신하기 위해 사용
	
	
}
