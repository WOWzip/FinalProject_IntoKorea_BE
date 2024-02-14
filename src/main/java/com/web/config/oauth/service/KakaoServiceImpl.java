package com.web.config.oauth.service;
import java.util.Optional;

import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.config.PrincipalDetailsService;
import com.web.domain.KakaoProfile;
import com.web.domain.Member;
import com.web.domain.OAuthToken;
import com.web.domain.Role;
import com.web.persistence.MemberRepository;

/** 카카오로부터 Token 받아오기, Token으로 사용자 정보 받아오기 **/
@PropertySource("classpath:application.properties")
@Service
public class KakaoServiceImpl implements KakaoService{
	
	 @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	 private String id;
	 
	 
	 @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	 private String secret;
	 
	 
	 @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	 private String redirect_uri;
	
	 @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	 private String token_uri;
	 
	 @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	 private String user_info_uri;
	 
	 
	 
	
	/** 요청1) 카카오 '토큰 발급 url'로부터 Token 요청 **/
    public OAuthToken getKakaoAccessToken (String code) {
    	
        
        //post방식으로 key=value 데이터를 요청(카카오한테)
		RestTemplate rt = new RestTemplate(); // http요청을 쉽게 할 수 있는 라이브러리
		
		//HttpHeader 오브젝트 생성
		HttpHeaders headers= new HttpHeaders();
		headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		

		//HttpBody 오브젝트 생성
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", id);
		params.add("client_secret", secret);
		params.add("redirect_uri", redirect_uri);
		params.add("code", code);
		
		
		//HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);
	
		
		//Http 요청하기 - Post방식으로 그리고 response 변수의 응답받음. (카카오로부터 토큰 받아오기)
		ResponseEntity<String> response =rt.exchange(
			token_uri,  							//토큰 발급 요청 주소
		    HttpMethod.POST,						//요청 메서드 post
		    kakaoTokenRequest,
		    String.class							// 응답받을 타입
			);
		
		// System.out.println("카카오 토큰 요청 완료 : 토큰 요청에대한 응답 :  " + response);
		
		
		// Gson, Json Simple, ObjectMapper ( Json 형식으로 바꿔서 OAuthToken 객체에 토큰 정보 저장하기)
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			 oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		System.out.println("토큰 json 형식으로 변환 : " + oauthToken);
		System.out.println("카카오 액세스 토큰 : " + oauthToken.getAccess_token()); // 변수명이 json형식의 키값과 일치해야함.
		
		return oauthToken;
		
    }
    
 
    
    
    
    
    /** 요청2) 카카오 '사용자 정보 url'로부터 사용자 정보 요청 . Token이용 **/
    public KakaoProfile getKakaoInfo(String accessToken) {
		
		RestTemplate rt2 = new RestTemplate(); // 요청 url 처리
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+ accessToken);
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				user_info_uri,  		// 사용자 정보 요청 주소
				HttpMethod.POST,
				kakaoProfileRequest2, 
				String.class);
//		System.out.println(response2);
//		return  response2.getBody(); //사용자 정보 결과가 담겨서 보여질 것
	
		
		// Gson, Json Simple, ObjectMapper ( Json 형식으로 바꿔서 KakaoProfile 객체에 사용자 정보 저장하기)
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (JsonProcessingException e) {	
			e.printStackTrace();
		}
        System.out.println("카카오아이디(번호) : "+kakaoProfile.getId() + 
		" 카카오 이메일 :" + kakaoProfile.getKakao_account().getEmail() + "닉네임 : " + kakaoProfile.getProperties().getNickname());
		
        
        return kakaoProfile;
    	
    }
    
    /** 로그인 및 회원가입 처리 **/
    
    //생성자주입
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
	private  PrincipalDetailsService principalDetailsService;
    
    
    public int MemberCheck(KakaoProfile kakaoProfile) {
    	
    	
    	// 회원 정보 받아온 값 저장
		String provider = "kakao"; // google, naver 등 OAuth 종류
		String providerId = String.valueOf(kakaoProfile.getId()); 
		String email = kakaoProfile.getKakao_account().getEmail();
		String password = bCryptPasswordEncoder.encode("12345"); // 임의의 비밀번호 암호화. (OAuth에서는 비번 입력할 일 없음. 임의로 정해주면 됨)
		Role role = Role.ROLE_USER;
		String nickName = kakaoProfile.getProperties().getNickname() + "@userK" + providerId; 
    	
    	
        // 이미 회원가입 되어 있는지 여부 확인
 		Optional<Member> optional = memberRepository.findByEmail(kakaoProfile.getKakao_account().getEmail());
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
 			
 			System.out.println("회원가입 완료");
 			return 0;
 			
 		} else {	
 			
 			Optional<Member> optional2 = memberRepository.findByEmailAndProvider(email, provider);
 			
 			if(optional2.isEmpty()) { 	// 다른 소셜에 이미 존재하는 이메일을 사용할 경우, 가입 x (해당 이메일의 provider와 일치한게 없으면, 이미 존재하는 이메일이므로 가입x)
 				
 				System.out.println("이미 존재하는 이메일입니다. 다른 계정으로 가입해주세요.");
 				return 1;
 			
 			} else {					// 본인의 메일로 접속할 경우, 로그인시키기
 				
 				System.out.println("자동 로그인을 진행합니다.");
 				memberEntity = optional.get();
 				System.out.println("memberEntity 값에는 어떤게 들어갈까 : " + memberEntity);
 				
 				//자동로그인 진행
 		 		UserDetails userDetail = principalDetailsService.loadUserByUsername(memberEntity.getEmail());
 				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
 				SecurityContext securityContext = SecurityContextHolder.getContext();
 				securityContext.setAuthentication(authentication);
 	            
 				return 2;
 			
 			}
 			
 		}
    	
    }
    
}