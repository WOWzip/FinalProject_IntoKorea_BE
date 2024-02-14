package com.web.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.FileEntity;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
 
}