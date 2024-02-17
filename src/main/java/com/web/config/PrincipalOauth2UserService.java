/** 소셜 로그인 **/

package com.web.config;

import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.web.config.oauth.provider.GoogleUserInfo;
import com.web.config.oauth.provider.NaverUserInfo;
import com.web.config.oauth.provider.KakaoUserInfo;
import com.web.config.oauth.provider.OAuth2UserInfo;
import com.web.domain.Member;
import com.web.domain.Role;
import com.web.persistence.MemberRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@Autowired
	private MemberRepository memberRepository;
	
	// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest: " + userRequest);
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); // registrationId='google'로 어떤 OAuth로 로그인했는지 확인 가능 (naver, google, kakao 등)
		System.out.println("getTokenValue: " + userRequest.getAccessToken().getTokenValue());
		
		
		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth - Client 라이브러리) -> AccessToken 요청
		// userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아줌.
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("getAttributes: " + oAuth2User.getAttributes());
		
		// OAuth 로그인 (회원가입 안되어 있으면, 회원가입을 강제로 진행)
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes()); // GoogleUserInfo.java에서 provider, providerId, email, name값을 뽑아냄
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response")); // NaverUserInfo.java에서 response객체 안에 있는 response객체(id, email, name)의 값을 뽑아냄
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"), 	// KakaoUserInfo.java에서 Map(key, value)을 이용하여 kakao_account객체 안에 있는 email의 값을 뽑아냄
					String.valueOf(oAuth2User.getAttributes().get("id"))); 						
		} 
		else {
			System.out.println("지원하지 않는 로그인 서비스입니다. 우리는 구글, 네이버, 카카오만 지원합니다.");
		}
		
		
		// 회원 정보 받아온 값 저장
		String provider = oAuth2UserInfo.getProvider(); // google, naver 등 OAuth 종류
		String providerId = oAuth2UserInfo.getProviderId(); // sub(google) 혹은 id(기타) 값 
		String email = oAuth2UserInfo.getEmail();
		String password = bCryptPasswordEncoder.encode("12345"); // 임의의 비밀번호 암호화. (OAuth에서는 비번 입력할 일 없음. 임의로 정해주면 됨)
		Role role = Role.ROLE_USER;
		String nickName = oAuth2UserInfo.getNickName(); 
		
		
		// 이미 회원가입 되어 있는지 여부 확인
		Optional<Member> optional = memberRepository.findByEmail(email);
		Member memberEntity = new Member();
		
		// 최초인 경우 회원가입 시키기
		if(optional.isEmpty()) { 
			System.out.println("OAuth 로그인이 최초입니다.");
			
			memberEntity = Member.builder()
					.email(email)
					.password(password)
					.role(role)
					.nickName(nickName)
					.provider(provider)
					.providerId(providerId)
					.build();
			memberRepository.save(memberEntity);
			
		} else {	
			
			Optional<Member> optional2 = memberRepository.findByEmailAndProvider(email, provider);
			if(optional2.isEmpty()) { 	// 이미 존재하는 이메일을 사용할 경우, 가입 x (해당 이메일의 provider와 일치한게 없으면, 이미 존재하는 이메일이므로 가입x)
				System.out.println("이미 존재하는 이메일입니다. 다른 이메일로 가입해주세요.");
			} else {					// 본인의 메일로 접속할 경우, 로그인시키기
				System.out.println("로그인");
				memberEntity = optional.get();
				System.out.println("memberEntity 값에는 어떤게 들어갈까 : " + memberEntity);
			}
			
		}
		
		// PrincipalDetails가 Authentication 객체에 세션정보로 들어감(?)
		return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
	}
		
	
}
