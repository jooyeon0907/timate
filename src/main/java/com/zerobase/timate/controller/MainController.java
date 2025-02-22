package com.zerobase.timate.controller;

import com.zerobase.timate.compoent.MailComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

	private final MailComponent mailComponent;

	@GetMapping("/")
	public String index() {
		return "hello";
	}

	@GetMapping("/test-send-mail")
	public String testSendMail() {

//		mailComponent.sendMailTest();

		String email = "test@naver.com";
		String subject = "안녕하세요. 제로베이스 입니다.";
		String text = "<p>안녕하세요. 제로베이스 입니다.</p><p>반갑습니다.</p>";
		mailComponent.sendMail(email, subject, text);

		return "send mail";
	}
}
