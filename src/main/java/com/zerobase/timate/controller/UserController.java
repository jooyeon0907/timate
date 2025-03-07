package com.zerobase.timate.controller;


import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.dto.UserDto.UpdateGroup;
import com.zerobase.timate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> info(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(userService.getUserInfo(id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> update(@PathVariable Long id,
						  @Validated(UpdateGroup.class) @RequestBody UserDto.Request request) {
		request.setId(id);
		return ResponseEntity.ok(ApiResponse.success(userService.updateUser(request)));
	}

	// TODO: 탈퇴

}
