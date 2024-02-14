package com.web.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

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

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RedisUtil redisUtil;
	
    private final JavaMailSender javaMailSender;

    // 인증번호 생성
    private final String ePw = createKey();
    
    // 임시 비밀번호 생성
    private final String tempPwd = getTempPassword();

    @Value("${spring.mail.username}")
    private String email;

    /** 회원가입_인증 코드 생성 **/
    // 인증번호 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
    
    // 안내 메시지1
    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : " + ePw);
        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("[IntoKorea] 회원가입 인증 코드 안내"); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">IntoKorea 회원가입 인증 코드 안내</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(email,"intoKorea")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }
    
    
    /** 비번 찾기 **/
    // 임시 비밀번호 만들기 (10자리의 랜덤난수를 생성하는 메소드)
    public static String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String tempPwd = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            tempPwd += charSet[idx];
        }
        return tempPwd;
    }
    
    // 안내 메시지2
    @Override
    public MimeMessage createPwdMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        log.info("임시 비밀번호 : " + tempPwd);
        MimeMessage  message = javaMailSender.createMimeMessage();

        String nickName = memberRepository.findByEmail(to).get().getNickName();
        
        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("[IntoKorea] 임시 비밀번호 안내"); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">IntoKorea 임시 비밀번호 안내</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">안녕하세요. IntoKorea 임시 비밀번호 안내 이메일 입니다.<p/>"
        		+"<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">" + nickName + "님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += tempPwd;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(email,"intoKorea")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    
    // 이메일로 발송된 임시비밀번호로 해당 유저의 패스워드 변경
    @Override
    public void updatePassword(String userEmail, String tempPwd){
        memberService.updatePwd(userEmail, tempPwd);
    }

    
    
    /** 아이디 찾기 **/
    // 안내 메시지3
    @Override
    public MimeMessage createIdMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        MimeMessage  message = javaMailSender.createMimeMessage();

        String provider = memberRepository.findByEmail(to).get().getProvider();
        
        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("[IntoKorea] 등록된 아이디 계정 안내"); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">IntoKorea 아이디 계정 안내</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">안녕하세요. IntoKorea 아이디 계정 안내 이메일 입니다.<p/>"
        		+"<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\"> 현재 IntoKorea에 등록된 아이디 "
        		+ to
        		+ " 은(는) </p>";
        		if(provider.equals("none")) {
        			msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">"
        					+ "일반 계정으로 가입된 아이디입니다."    
        		        	+ "</td></tr></tbody></table></div>"
        					+ "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">일반 계정으로 로그인을 시도해주세요.</p>";
        		} else {
        			msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">"
        					+ provider
        					+ " 계정으로 가입된 아이디입니다."    
        		        	+ "</td></tr></tbody></table></div>"
        					+ "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">"
        					+ provider + " 계정으로 로그인을 시도해주세요. </p>";
        		}
        		

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(email,"intoKorea")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }
    
    
    // 메일 발송1 (회원가입_인증번호)
    @Override
    public String sendCodeMessage(String to) throws Exception {
    	MimeMessage message = createMessage(to);
        try {
        	System.out.println("ePW : " + ePw);
        	System.out.println("받는 이메일: " + to);
            redisUtil.setDataExpire(ePw, to, 3*60L); // 유효시간 3분 (유효 시간 설정하여 Redis에 저장)
            javaMailSender.send(message); // 메일 발송
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }
    
    // 메일로 발송된 인증번호 유효한 인증번호인지 확인
    @Override
    public boolean verifyCode(String key) throws NotFoundException {
        String memberEmail = redisUtil.getData(key);
        if (memberEmail == null) {
        	System.out.println("유효하지 않은 인증번호입니다.");
        	return false;
        }
        System.out.println("입력받은 키 : "+ key);
        redisUtil.deleteData(key);
        return true;
    }
    

    // 메일 발송2(임시 비번 안내)
    @Override
    public String sendPwdMessage(String to) throws Exception {
        MimeMessage message = createPwdMessage(to);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return tempPwd; // 메일로 보냈던 임시 비밀번호를 서버로 리턴
    }
    
    
    // 메일 발송3(아이디 안내)
    @Override
    public void sendIdMessage(String to) throws Exception {
    	MimeMessage message = createIdMessage(to);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

    }
    
}



/*
	메일 발송
	sendPwdMessage의 매개변수로 들어온 to는 임시 비밀번호를 받을 메일주소
	MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
	bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
*/

/* 
 * 이메일 내용 작성 및 발송 클래스 구현
 * 
 * sendSimpleMessage는 실제 메일을 발송하는 메서드고 
 * TempPwd를 return하는 이유는 메일로 보낸 인증 코드를 서버에서 저장하고 있다가 사용자가 인증번호를 입력하였을 때 인증하기 위함이다.
 */

