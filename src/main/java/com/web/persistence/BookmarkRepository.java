package com.web.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.web.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {
	
	List<Bookmark> findByEmail(String Email);
	
	
	@Query("SELECT COUNT(e) FROM Bookmark e WHERE e.email = :value1 AND e.contentid = :value2")
    int bookmarkCheck(String value1, String value2);
	
	
	Bookmark findByEmailcontentid(String emailcontentid);
	
	
	
}
