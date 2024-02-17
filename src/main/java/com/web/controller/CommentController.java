package com.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.domain.Comment;
import com.web.service.CommentService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/mypage")
public class CommentController {
	
	@Autowired
	private CommentService commentService;

	// 댓글작성
	@PostMapping("/comments/add")
    public ResponseEntity<String> addComment(@RequestBody Comment comment) {
        Long askSeq = comment.getAsk().getSeq();
        commentService.addComment(comment);
        return ResponseEntity.ok("댓글이 성공적으로 등록되었습니다.");
    }
	
	// 댓글리스트
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getCommentsByAskSeq(@RequestParam Long askSeq) {
        List<Comment> comments = commentService.getCommentsByAskSeq(askSeq);
        return ResponseEntity.ok(comments);
    }
    
    // 댓글 삭제
    @Transactional
    @DeleteMapping("/comments/delete/{commentSeq}")
    public void deleteComment(@PathVariable Long commentSeq) {
    	commentService.deleteAnswerBycommentSeq(commentSeq);
    }
    

}