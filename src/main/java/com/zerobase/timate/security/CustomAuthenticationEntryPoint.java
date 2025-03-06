package com.zerobase.timate.security;

import static com.zerobase.timate.type.ErrorCode.INVALID_USER_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.timate.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// JWT 토큰 인증 실패 시, Spring Security 가 기본적으로 권한이 없다는 403 에러를 반환함
// AuthenticationEntryPoint 를 커스터마이즈 하여 인증 실패 시, 원하는 응답 주기
// -> SecurityConfig 에 추가 설정해야됨
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		// 401 상태 코드 설정
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ApiResponse apiResponse = ApiResponse.error(INVALID_USER_TOKEN);

		// JSON 형식으로 ApiResponse 객체를 응답 본문에 작성
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

		// 상태 코드와 메시지를 로그로 출력 (디버깅용)
		log.info("Response Status: " + response.getStatus());
		log.info("Response Message: " + INVALID_USER_TOKEN.getMessage());
	}
}
