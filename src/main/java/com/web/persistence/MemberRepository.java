package com.web.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.Member;

// CRUD 함수를 JpaRepository가 들고 있음.
// public interface MemberRepository extends JpaRepository<Member, MemberId>{
public interface MemberRepository extends JpaRepository<Member, Long>{

	/** JPA Query Methods **/
	
	// 이메일로 조회
	// select * from member where email = ?
	Optional<Member> findByEmail(String email);
	
	// 닉네임으로 조회
	Optional<Member> findByNickName(String nickName);
	
	// provider로 조회
	Optional<Member> findByEmailAndProvider(String email, String provider);
	
}
