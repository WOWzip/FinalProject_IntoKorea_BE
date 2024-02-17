package com.web.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.Ask;
import com.web.domain.Member;

public interface AskRepository extends JpaRepository<Ask, Long> {
   
	// Q&A 삭제
	void deleteBySeq(Long seq);
	
	// Q&A 수정
	Ask findBySeq(Long seq);
	
	// SEQ 높은순서로 목록
	List<Ask> findAllByOrderBySeqDesc();
}