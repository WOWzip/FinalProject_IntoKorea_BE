package com.web.config.oauth.provider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes; // oAuth2User.getAttributes()의 값임
	
	public GoogleUserInfo(Map<String, Object> attributes) {
		this.attributes=attributes;
	}
	
	@Override
	public String getProviderId() {
		return (String) attributes.get("sub"); // google에서 사용자에게 부여하는 pk값(sub)
	}

	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getNickName() {
		return (String) attributes.get("name");
	}

	
	
}
