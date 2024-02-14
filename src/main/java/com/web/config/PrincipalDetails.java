package com.web.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.web.domain.Member;
import com.web.domain.Role;

import lombok.Data;



// 시큐리티가 "/login" 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료되면 시큐리티 session을 만들어줍니다. (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 함.
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

// spring-security에서 유저인증에 사용됨.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private Member member; // 콤포지션
	private Map<String, Object> attributes;

	// 일반 로그인
	public PrincipalDetails(Member member) {
		this.member = member;
	}
	
	// OAuth 로그인
	public PrincipalDetails(Member member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes = attributes;
		System.out.println("attribute 잘 받아왔는지 확인" + attributes); // 확인 ok
		
	}
	
	// [ Spring Security 필수 메서드 재정의 ]_UserDetails 인터페이스 재정의
	// 해당 User의 권한(role)을 리턴하는 곳 
	// SecurityFilterChain에서 권한을 체크할 때 사용됨
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return String.valueOf(member.getRole());
			}
		});
		return collect;
	}

	
	// 사용자 인증에 사용되는 암호 반환
	@Override
	public String getPassword() {
		return member.getPassword();
	}

	// 사용자 인증에 사용되는 사용자 이메일 반환
	@Override
	public String getUsername() {
		return member.getEmail(); // email값을 username으로 저장
	}

	// 계정 만료되지 않았는지
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠기지 않았는지
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 자격증명(암호)이 만료되지 않았는지
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화되었는지
	@Override
	public boolean isEnabled() {
		return true;
	}

	
	// [ OAuth Client 필수 메서드 재정의 ]_OAuth2User 인터페이스 재정의
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}

/*
 * 컨트롤러에서 필요한 Authentication객체를 가져올 때, 일반 로그인이든 OAuth2 로그인이든 상관없이
 * 동일한 객체를 가져올수 있게 하기 위해서 함께 상속받았다.
 */
