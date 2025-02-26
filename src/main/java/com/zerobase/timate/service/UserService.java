package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.ALREADY_AUTH;
import static com.zerobase.timate.type.ErrorCode.FAILED_AUTH;
import static com.zerobase.timate.type.ErrorCode.USER_NOT_FOUND;

import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.exception.AuthException;
import com.zerobase.timate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;


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

	public UserDto.Response getUserInfo(Long id) {
		User user = getUser(id);
		return UserDto.Response.from(user);
	}

	public UserDto.Response updateUser(UserDto.Request request) {
		User user = getUser(request.getId());
		user.setName(request.getName());
		userRepository.save(user);

		return UserDto.Response.from(user);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));
	}

}
