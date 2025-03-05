package com.zerobase.timate.service;

import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final CommonService commonService;
	private final UserRepository userRepository;

	public UserDto.Response getUser(Long id) {
		User user = commonService.getUserById(id);
		return UserDto.Response.from(user);
	}

	public UserDto.Response updateUser(UserDto.Request request) {
		User user = commonService.getUserById(request.getId());
		user.setName(request.getName());
		userRepository.save(user);

		return UserDto.Response.from(user);
	}

}
