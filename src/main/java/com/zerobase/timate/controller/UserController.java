package com.zerobase.timate.controller;


import static com.zerobase.timate.type.SuccessCode.EMAIL_AUTH_SUCCESS;
import static com.zerobase.timate.type.SuccessCode.SIGNUP_SUCCESS;

import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.SignUpForm;
import com.zerobase.timate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpForm form) {
		userService.signUp(form);
		return ResponseEntity.ok(ApiResponse.success(SIGNUP_SUCCESS));
	}

	@GetMapping("/email-auth")
	public ResponseEntity<ApiResponse> emailAuth(HttpServletRequest request) {
		String uuid = request.getParameter("id");
		userService.emailAuth(uuid);
		return ResponseEntity.ok(ApiResponse.success(EMAIL_AUTH_SUCCESS));
	}


}
