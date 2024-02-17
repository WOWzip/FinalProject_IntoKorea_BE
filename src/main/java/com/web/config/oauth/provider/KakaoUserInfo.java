package com.web.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
	
	private String id;
	private Map<String, Object> kakaoAccount; // oAuth2User.getAttributes()의 값임
	
	public KakaoUserInfo(Map<String, Object> attributes, String id) {
		this.kakaoAccount=attributes;
		this.id = id;
	}
	
	@Override
	public String getProviderId() {
		return id;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		String email = String.valueOf(kakaoAccount.get("email"));
		System.out.println("kakao_account.email :::: " + email);
		return email;
	}

	@Override
	public String getNickName() {
		String nickName = String.valueOf(((Map<String, Object>) kakaoAccount.get("profile")).get("nickname"));
		System.out.println("kakao_account.profile.nickname값 :::: " + nickName);
		return nickName;
	}
	
}
