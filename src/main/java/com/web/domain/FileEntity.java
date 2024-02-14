package com.web.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "FileEntity")
public class FileEntity {

    @Id
    @GeneratedValue
    private Long fileId;

    private String fileName;
    private String filePath;
}