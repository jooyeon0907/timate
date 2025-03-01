package com.zerobase.timate.controller;


import static com.zerobase.timate.type.SuccessCode.EMAIL_AUTH_SUCCESS;
import static com.zerobase.timate.type.SuccessCode.LOGOUT_SUCCESS;
import static com.zerobase.timate.type.SuccessCode.SIGNUP_SUCCESS;

import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.SignInForm;
import com.zerobase.timate.dto.SignUpForm;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.security.TokenProvider;
import com.zerobase.timate.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final CommonService commonService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

	@PostMapping("/sign-up")
	public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpForm form) {
		return ResponseEntity.ok(ApiResponse.success(authService.signUp(form)));
	}

	@GetMapping("/email-auth")
	public ResponseEntity<ApiResponse> emailAuth(HttpServletRequest request) {
		String uuid = request.getParameter("id");
		authService.emailAuth(uuid);
		return ResponseEntity.ok(ApiResponse.success(EMAIL_AUTH_SUCCESS));
	}


	@PostMapping("/sign-in")
    public ResponseEntity<String> login(@Valid @RequestBody SignInForm form) {
		// 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));

		Long userId = commonService.getUserIdByEmail(form.getEmail());
		String token = tokenProvider.generateToken(form.getEmail(), userId);
        return ResponseEntity.ok(token);
    }

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
		SecurityContextHolder.clearContext();  // 인증 정보 삭제

		return ResponseEntity.ok(ApiResponse.success(LOGOUT_SUCCESS));
	}

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // 현재 로그인된 사용자 정보 (userDetails) 확인 가능
        String username = userDetails.getUsername();
		return ResponseEntity.ok("Hello " + username);
    }

}
