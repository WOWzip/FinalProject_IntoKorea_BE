package com.web.config.oauth.service;

import com.web.domain.KakaoProfile;
import com.web.domain.OAuthToken;

/** 카카오로부터 Token 받아오기, Token으로 사용자 정보 받아오기 **/

public interface KakaoService{
	
	/** 요청1) 카카오 '토큰 발급 url'로부터 Token 요청 **/
    public OAuthToken getKakaoAccessToken (String code);
    
    /** 요청2) 카카오 '사용자 정보 url'로부터 사용자 정보 요청 . Token이용 **/
    public KakaoProfile getKakaoInfo(String accessToken);
    
    /** 로그인 및 회원가입 처리 **/
    public int MemberCheck(KakaoProfile kakaoProfile);
    
    
}