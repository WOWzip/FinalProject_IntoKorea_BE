package com.web.service;

import com.web.domain.Member;

public interface MemberService {
	
	// 회원가입
	void registerMember(Member memberEntity);
	
	// 이메일 체크
	boolean checkEmail(String userEmail);
	
	// 닉네임 체크
	boolean checkNickName(String userNickName);
	
	
	// 이메일 존재 체크
	public int userEmail(String email);
	
//	// 임시 비밀번호 발송
//	public String mailConfirm(String email);
	
	// 회원 조회
	 public Member findByEmail(String email);
	 
	// 회원 수정 (닉네임 수정)
	 public void updateNickName(String email, String nickName);
	 
	// 회원 수정 (비번 수정)
	public void updatePwd(String email, String Pwd);
	 
	 // 회원 삭제
	 public void delete(String email);
	 
	 
	 
	 
}
