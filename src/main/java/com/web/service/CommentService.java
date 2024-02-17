package com.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.domain.Comment;

@Service
public interface CommentService {
	
	// 댓글 작성
	public void addComment(Comment comment);
	
	// 댓글 리스트
	List<Comment> getCommentsByAskSeq(Long askSeq);
	
	// 댓글 삭제
	public void deleteAnswerBycommentSeq(Long commentSeq);

	
}
