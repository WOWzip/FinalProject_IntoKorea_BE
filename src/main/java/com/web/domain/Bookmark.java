package com.web.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Bookmark {

	@Id
	private String emailcontentid;
	
	private String email;
	private String firstimage;
	private String title;
	private String addr1;
	private String zipcode;
	private String contentid;
	
	
}
