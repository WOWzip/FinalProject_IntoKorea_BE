


package com.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	
	
}





