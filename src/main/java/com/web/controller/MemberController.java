package com.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.domain.Member;
import com.web.persistence.MemberRepository;
import com.web.service.EmailService;
import com.web.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private final EmailService emailService;


	/** 일반 회원가입 **/

	// 회원가입(React와 연동)
	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody Member memberEntity) throws IOException {
		
		System.out.println("memberEntity : " + memberEntity);
		memberService.registerMember(memberEntity);

		return new ResponseEntity<>("Member registered successfully", HttpStatus.OK);
	}
	
	
	// 인증번호 발급
	@PostMapping("/emails/sendCode")
    public String sendCode(@RequestBody Member member) throws Exception {
		
		String email = member.getEmail();
		System.out.println("입력받은 email : "+ email);
        String ePw = emailService.sendCodeMessage(email);

        return ePw;
    }
	
	
	// 인증번호 유효성 체크
	@GetMapping("/emails/verifyCode")
    public boolean verifyCode(@RequestParam("code") String authCode, @RequestParam("userEmail") String userEmail) throws NotFoundException {
		System.out.println("입력받은 키:::" + authCode + "입력된 이메일 : " + userEmail);
        boolean verifyCode = emailService.verifyCode(authCode, userEmail);

        return verifyCode;
    }

	
	
	
	/** 일반 로그인 **/
	@PostMapping("/userLogin")
	public HashMap<String, String> userLogin(@RequestBody Member membercheck) {
		String email = membercheck.getEmail();
		String password = membercheck.getPassword();
		HashMap<String, String> map = new HashMap<>();
		Optional<Member> optional = memberRepository.findByEmail(email);

		System.out.println(optional);
		
		// 존재하지 않는 이메일일 경우
		if (optional.isEmpty()) { 
			map.put("check", "0");
			map.put("msg", "Email를 확인해 주세요");
			return map;
		}

		// 이메일이 존재할 경우, 
		// 계정 체크 
		Member member = optional.get();
		if(!member.getProvider().equals("none")) {
			map.put("check", "1");
			map.put("msg", "소셜로 가입된 계정입니다.");
			map.put("provider", member.getProvider());
			return map;
		}
		
		
		// 패스워드 체크
		boolean checkPassword = passwordEncoder.matches(password, member.getPassword()); // 입력한 패스워드가 맞으면 true 반환
		System.out.println(checkPassword);
		if (checkPassword == false) {
			map.put("check", "21");
			map.put("msg", "비밀번호를 확인해 주세요");
			return map;
		} else {
			map.put("msg", "로그인 성공");
			map.put("check", "22");
			map.put("nickName", member.getNickName());
			map.put("provider", member.getProvider());
			return map;
		}
	}
	
	
	/** 이메일 및 닉네임 존재여부(중복) 체크 **/
	
	// 이메일 확인(React와 연동)
	@PostMapping("/check/existEmail")
	public ResponseEntity<Boolean> checkEmail(HttpServletRequest request, @RequestBody Member member) throws ServletException, IOException {
		
		// 받아온 Email 출력
		String userEmail = member.getEmail();
		System.out.println("Email : " + userEmail);
		
		boolean checkEmail = memberService.checkEmail(userEmail); // false : 없는 값 (중복 x)

		return new ResponseEntity<Boolean>(checkEmail, HttpStatus.OK);
	}
	
	
	
	
	// 닉네임 확인(React와 연동)
	@PostMapping("/check/existNickName")
	public ResponseEntity<Boolean> checkNickName(HttpServletRequest request, @RequestBody Member member) throws ServletException, IOException {
		
		// 받아온 닉네임 출력
		String userNickName = member.getNickName();
		System.out.println("NickName : " + userNickName);
		
		boolean checkNickName = memberService.checkNickName(userNickName); // false : 없는 값 (중복 x)

		return new ResponseEntity<>(checkNickName, HttpStatus.OK);
	}
	
	

	/** 아이디 및 비밀번호 찾기 **/
	// 이메일 존재 확인
	@PostMapping("/find/userEmail")
	public int userEmail(@RequestBody Member member) throws IOException {
		
		String email = member.getEmail();
		System.out.println("email:"+ email);
		
		int findUserEmail = memberService.userEmail(email);
		
		return findUserEmail;
	}
	
	// 아이디 찾기 (이메일API활용_어떤 계정으로 가입했는지 안내)
	@PostMapping("/find/mailConfirmId")
    public void mailConfirmId(@RequestBody Member member) throws Exception {
    	String email = member.getEmail();
        emailService.sendIdMessage(email);
    }
	
	// 비밀번호 찾기 (이메일API활용_임시 비밀번호 전송)
    @PostMapping("/find/mailConfirmPwd")
    public String mailConfirmPwd(@RequestBody Member member) throws Exception {
    	String email = member.getEmail();
        String code = emailService.sendPwdMessage(email);
        System.out.println("임시 비밀번호 : " + code);
        emailService.updatePassword(email, code);
        return code;
    }
    
   
    
    /** 비밀번호 변경 **/
    // 1. 비밀번호 학인
    @PostMapping("/checkPwd")
	public HashMap<String, String> checkPwd(@RequestBody Member membercheck) {
		String email = membercheck.getEmail();
		String password = membercheck.getPassword();
		System.out.println("email : " +  email);
		HashMap<String, String> map = new HashMap<>();
		Optional<Member> optional = memberRepository.findByEmail(email);

		System.out.println(optional);
		
		// 존재하지 않는 이메일일 경우
		if (optional.isEmpty()) { 
			map.put("check", "0");
			map.put("msg", "다시 로그인해 주세요");
			return map;
		}

		
		// 이메일이 존재할 경우, 
		// 계정 체크 
		Member member = optional.get();
		if(!member.getProvider().equals("none")) {
			map.put("check", "1");
			map.put("msg", "소셜로 가입된 계정입니다.");
			return map;
		}
		
		//패스워드 체크
		boolean checkPassword = passwordEncoder.matches(password, member.getPassword()); // 입력한 패스워드가 맞으면 true 반환
		System.out.println(checkPassword);
		if (checkPassword == false) {
			map.put("check", "21");
			map.put("msg", "비밀번호를 확인해 주세요");
			return map;
		} else {
			map.put("check", "22");
			map.put("msg", "비밀번호 확인 성공");
			return map;
		}
	}
    
    
    // 2. 비밀번호 변경
    @PostMapping("/modifyPwd")
    public void modifyPwd(@RequestBody Member member) throws Exception {
    	String email = member.getEmail();
    	String password = member.getPassword();
        memberService.updatePwd(email, password);
    }
    
    
    /** 닉네임 변경 **/
    
    @PostMapping("/modifyNickName")
    public void modifyNickName(@RequestBody Member member) throws Exception {
    	String email = member.getEmail();
    	String nickName = member.getNickName();
        memberService.updateNickName(email, nickName);
    }
    
    
    /** 회원 탈퇴 **/
    @PostMapping("/deleteUser")
    public boolean deleteUser(@RequestParam("userEmail") String userEmail) throws Exception {
    	System.out.println("입력받은 키 : " + userEmail);
    	boolean existUser = memberService.delete(userEmail);
    	return existUser;
    }

    
}