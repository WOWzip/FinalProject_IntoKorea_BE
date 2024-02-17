/** 로그인 **/

package com.web.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.domain.Member;
import com.web.persistence.MemberRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 으로 했기 때문에
// "/login" 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행됨.
// Authentication 객체로 만들어준다.
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private MemberRepository memberRepository;
	
	// 시큐리티 session(내부 Authentication(내부 UserDetails))
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
		
		System.out.println("userEmail : " + username);
		Optional<Member> optional = memberRepository.findByEmail(username);
		if(optional.isPresent()) {
			Member memberEntity = optional.get();
			System.out.println("멤버가 있는 경우");
			System.out.println("memberEntity:" + memberEntity);
			return new PrincipalDetails(memberEntity); // 시큐리티세션에 유저정보가 저장이 됨
		}
		System.out.println("해당 멤버가 없습니다." + username);
		return null;
	}

}
