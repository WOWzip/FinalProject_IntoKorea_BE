package com.web.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.domain.Member;
import com.web.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface EmailService {
	

    // 비밀번호 찾기 (임시 비밀번호 전송)
    public MimeMessage createPwdMessage(String to) throws MessagingException, UnsupportedEncodingException ;

    // 아이디 찾기 (아이디 계정 정보 메시지 전송)
    public  MimeMessage createIdMessage(String to) throws MessagingException, UnsupportedEncodingException ;
    
    // 임시비밀번호로 해당 유저의 패스워드 변경
    public void updatePassword(String userEmail, String tempPwd);

    
    // 메일 발송1 (회원가입_인증번호)
    public String sendCodeMessage(String to) throws Exception;
    
    // 인증번호 유효한지 체크
    public boolean verifyCode(String code, String userEmail) throws NotFoundException;
    
    
    // 메일 발송2 (임시 비밀번호)
    public String sendPwdMessage(String to) throws Exception;
    
    // 메일 발송3 (아이디 계정)
    public void sendIdMessage(String to) throws Exception;
    
}

