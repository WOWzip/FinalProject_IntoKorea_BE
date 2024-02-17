

package com.web.domain;

import java.sql.Date;

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
@Table(name="diary")
public class Diary {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIARY_SEQUENCE")
    @SequenceGenerator(name = "DIARY_SEQUENCE", sequenceName = "DIARY_SEQUENCE", allocationSize = 1)
    @Column(name = "SEQ")
    private Long seq;

    private String dtitle;
    
    private String dcontent;
    
    private String location;
    private String rating;
    
    private String dimage;
    private String dimagepath;
    
    private String theme;
    
    @Column(name = "visit_date")
    private Date visitDate;
    
    @Column(name = "finish_date")
    private Date finishDate;
    
    
    
	@JsonFormat(pattern="yyyy.MM.dd")
	@Column(insertable = false, updatable = false, columnDefinition = "date default sysdate")
	private Date ddate;   
	
	@Column(updatable = false)
	private String email;
	
}
