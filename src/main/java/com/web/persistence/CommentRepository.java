package com.web.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long >{
	
	// 댓글 목록
	List<Comment> findByAskSeq(Long askSeq);
	
	// 댓글 삭제
	void deleteAnswerBycommentSeq(Long commentSeq);

}
