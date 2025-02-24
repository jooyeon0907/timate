package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.ALREADY_AUTH;
import static com.zerobase.timate.type.ErrorCode.ALREADY_USER;
import static com.zerobase.timate.type.ErrorCode.FAILED_AUTH;

import com.zerobase.timate.compoent.MailComponent;
import com.zerobase.timate.dto.SignUpForm;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.exception.AuthException;
import com.zerobase.timate.repository.UserRepository;
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

	public void signUp(SignUpForm form) {
		if (userRepository.findByEmail(form.getEmail()).isPresent()) {
			throw new AuthException(ALREADY_USER);
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

	}

	public void emailAuth(String uuid) {
		User user = userRepository.findByEmailAuthKey(uuid).orElseThrow(
			() -> new AuthException(FAILED_AUTH)
		);

		if (user.isEmailAuthYn()) {
			throw new AuthException(ALREADY_AUTH);
		}

		user.setEmailAuthYn(true);
		userRepository.save(user);

	}
}
