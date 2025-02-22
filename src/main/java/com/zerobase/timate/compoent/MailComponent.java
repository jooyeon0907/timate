package com.zerobase.timate.compoent;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

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

	public boolean sendMail(String mail, String subject, String text) {
		boolean result = false;

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
			result = true;
		  }catch (Exception e){
			System.out.println("sendMail error : " + e.getMessage());
		  }

		return result;
	}

}
