package com.zerobase.timate.service;

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
public class CommonService {

	private final UserRepository userRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));
	}

}
