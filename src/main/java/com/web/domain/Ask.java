


package com.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Ask")
public class Ask {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASK_SEQUENCE")
    @SequenceGenerator(name = "ASK_SEQUENCE", sequenceName = "ASK_SEQUENCE", allocationSize = 1)
    @Column(name = "SEQ")
	private Long seq;
	private String title;
	private String content;
	@JsonFormat(pattern="yyyy.MM.dd")
	@Column(insertable = false, updatable = false, columnDefinition = "date default sysdate")
	private Date AskDate;
	
	// 파일첨부
	private String filename;
	private String filepath;
	
	@Column(updatable = false)
	private String email;
	
	@Column
	private String nickName;
	
	private String ready = "처리중";

    // 매니저 답변 여부
    private boolean answered = false;
    
    // 매니저 답변 완료 처리 메서드
    public void markAsAnswered() {
        this.ready = "답변완료";
        this.answered = true;
    }

	
}





