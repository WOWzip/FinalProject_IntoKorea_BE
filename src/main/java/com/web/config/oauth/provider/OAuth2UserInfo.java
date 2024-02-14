// 여러 소셜 로그인을 위한 설정(구글, 네이버 등 서로 제공해주는 사용자 정보가 다르기 때문에 각 소셜로그인 사이트에서 제공해주는 정보와 매핑해주는 작업 필요)

package com.web.config.oauth.provider;

public interface OAuth2UserInfo {
	// getAttributes() 를 통해 전달받은 변수들에 접근하기 위함
	String getProviderId();
	String getProvider();
	String getEmail();
	String getNickName();
}
