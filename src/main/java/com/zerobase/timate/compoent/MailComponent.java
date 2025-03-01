package com.zerobase.timate.compoent;

import static com.zerobase.timate.type.ErrorCode.FAILED_SEND_EMAIL;

import com.zerobase.timate.exception.AuthException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailComponent {

	private final JavaMailSender javaMailSender;

	public void sendMailTest() {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("test@naver.com");
		msg.setSubject("안녕하세요. 제로베이스 입니다.");
		msg.setText("안녕하세요. 제로베이스 입니다. 반갑습니다. ");

		javaMailSender.send(msg);

	}

	public void sendMail(String mail, String subject, String text) {
		MimeMessagePreparator msg = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo(mail);
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(text, true);
			}
		};

		try{
    		javaMailSender.send(msg);
		  }catch (Exception e){
        	log.error("sendMail error: {}", e.getMessage());
			throw new AuthException(FAILED_SEND_EMAIL);
		  }

	}

}
