package com.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.config.oauth.service.KakaoService;
import com.web.domain.KakaoProfile;
import com.web.domain.Member;
import com.web.domain.OAuthToken;
import com.web.persistence.MemberRepository;
import com.web.service.MemberService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

	@Autowired
    private KakaoService kakaoService;
	
    @Autowired
    private MemberService memberService;
    
	
	 /**
	  * OAuth 로직
	 * 1) 인가코드 받기
     *    프론트 [로그인] 
     * -> 카카오 [인가코드 전송] 
     * -> 프론트 redirect-uri로 인가코드 전송 (http://localhost:3000/login/oauth2/code/kakao) 
     * -> 프론트에서 다시 백으로 인가코드 전송
     * 
     * 2) 토큰 받기
     * -> 백에서 카카오로 [토큰요청]
     * -> 카카오에서 백으로 토큰 발급
     * 
     * 3) 사용자 로그인 처리
     * -> 토큰으로 사용자 정보 조회(이메일, 닉네임 등)
     * -> 회원인지 확인 및 회원가입/로그인 처리
     * -> 프론트에서 결과 처리 
     *
     **/

    @GetMapping("/login/kakao")	// 프론트에서 인증 코드 받아서 >> 카카오로 토큰 요청 >> 받은 토큰 프론트로 보내기
    public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam String code) {
    	Map<String, Object> response = new HashMap<>();
    	
        System.out.println(code);
        OAuthToken oauthToken = kakaoService.getKakaoAccessToken(code); // 카카오로부터 토큰 받아오기
        System.out.println("Token : " +  oauthToken);
        String accessToken = oauthToken.getAccess_token();
        System.out.println("Access Token : " +  accessToken);
        KakaoProfile kakaoProfile = kakaoService.getKakaoInfo(accessToken); // 토큰으로 사용자 정보 가져오기
        int memberCheck = kakaoService.MemberCheck(kakaoProfile);	// 회원가입 및 로그인 처리 (0:회원가입, 1:이미 존재하는 메일 2:로그인)
        System.out.println("회원가입 및 로그인 처리 :::" + memberCheck);
        String email = kakaoProfile.getKakao_account().getEmail();
        Member member = memberService.findByEmail(email);
        String nickName = member.getNickName();
        
        response.put("memberCheck", memberCheck);
        
        response.put("accessToken", accessToken);
        response.put("email", email);
        response.put("nickName" , nickName);
//        response.put("nickName", kakaoProfile.getProperties().getNickname());
      
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
