package com.web.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.Ask;

public interface AskRepository extends JpaRepository<Ask, Long> {
   
	// Q&A 삭제
	void deleteBySeq(Long seq);
	
	// Q&A 수정
	Ask findBySeq(Long seq);


}