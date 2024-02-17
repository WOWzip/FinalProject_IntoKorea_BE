package com.web.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
@Entity
//@IdClass(MemberId.class) //복합키 클래스  
public class Member {
	
	//(기본키)
	@Id
	@GeneratedValue 
	@Column(name = "MEMBER_SEQ")
	private Long seq;
	
	// 회원 이메일 
	
	@Column(name = "MEMBER_EMAIL", nullable = false, updatable = false, unique = true)
	private String email;
	
	// OAuth(소셜로그인) 종류(google, naver 등)
	@Column(name = "MEMBER_PROVIDER")
	private String provider;
	
	// 해당 OAuth의 Key(id)값
	@Column(name = "MEMBER_PROVIDERID")
	private String providerId;
	
	// 회원 비밀번호
	@Column(name = "MEMBER_PASSWORD", nullable = false)
	private String password;

	// 회원 역할 : ROLE_ADMIN, ROLE_USER
	@Column(name = "MEMBER_ROLE", nullable = false, updatable = false)
	@Enumerated(EnumType.STRING) // EnumType.STRING : enum 이름 값을 DB에 저장
	private Role role;
	
	// 회원 닉네임
	@Column(name = "MEMBER_NICKNAME", nullable = false)
	private String nickName;
	
	
	@Builder
	public Member(String email, String password, Role role, String nickName, String provider, String providerId) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.nickName = nickName;
		this.provider = provider;
		this.providerId = providerId;
	}

	@Override
	public String toString() {
		return "Member [email=" + email + ", password=" + password + ", role=" + role + ", nickName=" + nickName
				+ ", provider=" + provider + ", providerId=" + providerId + "]";
	}

	public Member(String nickName, String email) {
		this.nickName = nickName;
		this.email = email;
	}

}

// 참고) strategy 속성을 적지 않고 @GeneratedValue만 쓰면 GenerationType.AUTO가 적용되고, JPA에서 데이터베이스에 맞는 자동생성 전략을 선택합니다. (MYSQL은 IDENTITY, 오라클은 SEQUENCE)
