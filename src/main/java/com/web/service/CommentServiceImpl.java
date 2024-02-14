package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.domain.Comment;
import com.web.persistence.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepo;

	
	// 댓글 작성
	@Override
	public void addComment(Comment comment) {
	
		commentRepo.save(comment);
	}
	
	// 댓글 목록
    @Override
    public List<Comment> getCommentsByAskSeq(Long askSeq) {
        return commentRepo.findByAskSeq(askSeq);
    }

    //댓글 삭제
    @Override
    public void deleteAnswerBycommentSeq(Long commentSeq) {
    	commentRepo.deleteAnswerBycommentSeq(commentSeq);
    }
}
