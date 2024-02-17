package com.web.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes; // oAuth2User.getAttributes()의 값임
	
	public NaverUserInfo(Map<String, Object> attributes) {
		this.attributes=attributes;
	}
	
	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		return String.valueOf(attributes.get("email"));
	}

	@Override
	public String getNickName() {
		return String.valueOf(attributes.get("nickname"));
	}

	
	
}
