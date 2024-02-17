package com.web.domain;

import lombok.Data;

/** 카카오 사용자 정보 **/
//사용자 정보를 json형식으로 변환 -> json형식의 키값과 변수명이 일치해야함.
@Data
public class KakaoProfile {
	public Long id;
	public String connected_at;
	public Properties properties;
	public KakaoAccount kakao_account;
	
	@Data
	public class Properties {
		public String nickname;
	}

	@Data
	public class KakaoAccount {
		public Boolean profile_nickname_needs_agreement;
		public Profile profile;
		public Boolean has_email;
		public Boolean email_needs_agreement;
		public Boolean is_email_valid;
		public Boolean is_email_verified;
		public String email;
		
		@Data
		public class Profile {
			public String nickname;
		}
	}
}