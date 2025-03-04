package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.ALREADY_AUTH;
import static com.zerobase.timate.type.ErrorCode.ALREADY_USER;
import static com.zerobase.timate.type.ErrorCode.FAILED_AUTH;

import com.zerobase.timate.compoent.MailComponent;
import com.zerobase.timate.dto.SignUpForm;
import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.exception.AuthException;
import com.zerobase.timate.repository.UserRepository;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;
	private final MailComponent mailComponent;
    private final PasswordEncoder passwordEncoder;

	@Value("${SERVER_URL}")
	private String serverUrl;

	public UserDto.Response signUp(SignUpForm form) {
		if (userRepository.findByEmail(form.getEmail()).isPresent()) {
			throw new AuthException(ALREADY_USER);
		}

		String uuid = UUID.randomUUID().toString();

		User user = User.builder()
			.email(form.getEmail())
			.name(form.getName())
			.password(passwordEncoder.encode(form.getPassword()))
			.emailAuthKey(uuid)
			.build();

		userRepository.save(user);

		String email = form.getEmail();
		String subject = "Timate 가입을 축하드립니다.";
		String text = "<p>Timate 가입을 축하드립니다.</p>" +
				"<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
				"<div><a target='_blank' href='http://" + serverUrl + "/user/email-auth?id=" + uuid + "'>가입 완료</a></div>";
//		mailComponent.sendMail(email, subject, text);

		return UserDto.Response.from(user);
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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}



}
