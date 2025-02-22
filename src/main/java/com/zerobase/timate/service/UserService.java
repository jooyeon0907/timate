package com.zerobase.timate.service;

import com.zerobase.timate.compoent.MailComponent;
import com.zerobase.timate.dto.SignUpForm;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final MailComponent mailComponent;

	@Value("${SERVER_URL}")
	private String serverUrl;

	public String signUp(SignUpForm form) {
		Optional<User> optionalUser = userRepository.findByEmail(form.getEmail());
		if (optionalUser.isPresent()) {
			// TODO : custom exception
			return "이미 가입된 회원입니다.";
		}

		String uuid = UUID.randomUUID().toString();

		User user = User.builder()
			.email(form.getEmail())
			.name(form.getName())
			.password(form.getPassword()) // TODO: Spring Security 의 BCrypt 사용하여 password 암호화
			.emailAuthKey(uuid)
			.build();

		userRepository.save(user);

		String email = form.getEmail();
		String subject = "Timate 가입을 축하드립니다.";
		String text = "<p>Timate 가입을 축하드립니다.</p>" +
				"<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
				"<div><a target='_blank' href='http://" + serverUrl + "/user/email-auth?id=" + uuid + "'>가입 완료</a></div>";
		mailComponent.sendMail(email, subject, text);


		return "회원 가입에 성공하였습니다.";
	}

	public String emailAuth(String uuid) {
		Optional<User> optionalUser = userRepository.findByEmailAuthKey(uuid);
		if (!optionalUser.isPresent()) {
			return "인증이 실패되었습니다.";
		}

		User user = optionalUser.get();
		if (user.isEmailAuthYn()) {
			return "이미 인증된 회원입니다.";
		}

		user.setEmailAuthYn(true);
		userRepository.save(user);

		return "인증이 완료되었습니다";
	}
}
