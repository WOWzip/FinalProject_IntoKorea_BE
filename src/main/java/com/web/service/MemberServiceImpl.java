package com.web.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.domain.Member;
import com.web.domain.Role;
import com.web.persistence.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
//	@Autowired
//	private final EmailService emailService;
	
	/** 일반_회원가입 **/
	
	// 회원가입
	@Override
	public void registerMember(Member memberdEntity) {
		memberdEntity.setProvider("none"); // provider가 없으면 일반 로그인 (null값 대신 none문자열로 처리)
		memberdEntity.setRole(Role.ROLE_USER);
		String rawPasswword = memberdEntity.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPasswword);
		memberdEntity.setPassword(encPassword);
		System.out.println("memberdEntity: (세팅후)" + memberdEntity);
		memberRepository.save(memberdEntity);
	}
	
	
	/** 이메일 및 닉네임 존재여부 체크 **/
	
	// 이메일 체크
	@Override
	public boolean checkEmail(String userEmail) {

		Optional<Member> optional = memberRepository.findByEmail(userEmail);
		
		if (optional.isEmpty()) {
			return false;	// 존재하지 않음 (중복 x)
		} 
		String email = optional.get().getEmail();
		System.out.println("checkEmail :" + email);
		
		return true;		// 존재함 (중복)

	}
	
	
	// 닉네임 체크
	@Override
	public boolean checkNickName(String userNickName) {

		Optional<Member> optional = memberRepository.findByNickName(userNickName);
		
		if (optional.isEmpty()) {
			return false;	// 존재하지 않음 (중복 x)
		} 
		String nickName = optional.get().getNickName();
		System.out.println("checkNickname :" + nickName);
		
		return true;		// 존재함 (중복)
	}
	
	
	
	/** 아이디 및 비밀번호 찾기 **/
	// 이메일 존재 확인
	@Override
	public int userEmail(String email) {
		System.out.println("email:"+ email);
		Optional<Member> optional = memberRepository.findByEmail(email);

		System.out.println(optional);
		
		
		if (optional.isEmpty()) { 
			// 존재하지 않는 이메일일 경우(0)
			return 0;
		} else if(optional.get().getProvider().equals("none")) { 
			// 일반 로그인 계정일 경우(1)
			return 1;
		} else {
			// 다른 소셜 로그인 계정일 경우(2)
			return 2;
		}
	}
	
	
	/** User 조회 */
    @Transactional
    @Override
    public Member findByEmail(String email) {
    	Optional<Member> optional = this.memberRepository.findByEmail(email);
    	if(optional.isEmpty()) {
    		new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_email = " + email);
    		return null;
    	}
        return optional.get();
    }

    /** User 수정 */
    // 이메일로 닉네임 수정
    @Transactional
    @Override
    public void updateNickName(String email, String nickName) {
    	Optional<Member> optional = this.memberRepository.findByEmail(email);
    	if(optional.isEmpty()) {
    		new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_email = " + email);
    	}
    	Member member = optional.get();
    	member.setNickName(nickName);
    	memberRepository.save(member);
    }
    
    
    // 이메일로 비번 수정
    @Transactional
    @Override
    public void updatePwd(String email, String Pwd) {
    	String password = bCryptPasswordEncoder.encode(Pwd);
    	Optional<Member> optional = this.memberRepository.findByEmail(email);
    	if(optional.isEmpty()) {
    		new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_email = " + email);
    	}
    	Member member = optional.get();
    	member.setPassword(password);
    	memberRepository.save(member);
    }
    
    
    
    /** User 삭제 */
    @Transactional
    @Override
    public boolean delete(String email) {
    	Optional<Member> optional = this.memberRepository.findByEmail(email);
    	if(optional.isEmpty()) {
    		new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_email = " + email);
    		return false;
    	}
    	Member member = optional.get();
    	memberRepository.delete(member);
    	return true;
    }

}
