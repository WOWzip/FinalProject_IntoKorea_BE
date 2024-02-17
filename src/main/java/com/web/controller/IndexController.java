package com.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.config.PrincipalDetails;
import com.web.domain.Member;
import com.web.domain.Role;
import com.web.persistence.MemberRepository;
import com.web.service.MemberService;

@Controller
public class IndexController {
	
	@Autowired
	private MemberService memberService;
	
	@ResponseBody
	@GetMapping("/test/login")
	public String testLogin(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
		System.out.println("/test/login========================================");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getMember());
		
		System.out.println("userDetails : " + userDetails.getMember());
		return "세션 정보 확인하기";
	}
	
	@ResponseBody
	@GetMapping("/test/oauth/login")
	public String testOAuthLogin(Authentication authentication){ // DI(의존성 주입)
		System.out.println("/test/oauth/login========================================");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : " + oAuth2User.getAttributes());
		
		return "OAuth 세션 정보 확인하기";
	}
	
	
}
