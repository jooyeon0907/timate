package com.zerobase.timate.controller;


import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/info")
	public ResponseEntity<ApiResponse> info(@RequestParam Long id) {
		return ResponseEntity.ok(ApiResponse.success(userService.getUser(id)));
	}

	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody UserDto.Request request) {
		return ResponseEntity.ok(ApiResponse.success(userService.updateUser(request)));
	}

	// TODO: 탈퇴

}
