package com.zerobase.timate.controller;


import com.zerobase.timate.dto.SignUpForm;
import com.zerobase.timate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
	public ResponseEntity<String> signUp(@RequestBody SignUpForm form) {
		return ResponseEntity.ok(userService.signUp(form));
	}

	@GetMapping("/email-auth")
	public ResponseEntity<String> emailAuth(HttpServletRequest request) {
		String uuid = request.getParameter("id");

		return ResponseEntity.ok(userService.emailAuth(uuid));
	}


}
