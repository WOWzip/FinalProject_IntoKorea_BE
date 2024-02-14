
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
@Table(name = "Answer")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQUENCE")
	@SequenceGenerator(name = "COMMENT_SEQUENCE", sequenceName = "COMMENT_SEQUENCE", allocationSize = 1)
	@Column(name = "COMMENT_SEQ")
	private Long commentSeq;

	@ManyToOne
	@JoinColumn(name = "ASK_SEQ", referencedColumnName = "SEQ")
	private Ask ask; // 댓글이 어떤 질문(Ask)에 속하는지 연결

	private String content;

	@JsonFormat(pattern = "yyyy.MM.dd")
	@Column(insertable = false, updatable = false, columnDefinition = "date default sysdate")
	private Date commentDate;
}
