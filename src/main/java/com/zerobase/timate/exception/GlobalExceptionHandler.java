package com.zerobase.timate.exception;

import static com.zerobase.timate.type.ErrorCode.INTERVAL_SERVER_ERROR;
import static com.zerobase.timate.type.ErrorCode.INVALID_EMAIL_OR_PASSWORD;
import static com.zerobase.timate.type.ErrorCode.INVALID_REQUEST;

import com.zerobase.timate.dto.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ApiResponse> handleAccountException(AuthException e){
		log.error("{} is AuthException occurred.", e.getErrorMessage());

		return ResponseEntity.badRequest().body(ApiResponse.error(e.getErrorCode()));
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponse> handleUserException(UserException e){
		log.error("{} is UserException occurred.", e.getErrorMessage());

		return ResponseEntity.badRequest().body(ApiResponse.error(e.getErrorCode()));
	}

	// DTO 유효성 검사 실패 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> errors = new HashMap<>();

		// 모든 유효성 검사 에러 메시지 저장
		bindingResult.getFieldErrors().forEach( error ->
			errors.put(error.getField(), error.getDefaultMessage()));

		log.error("Validation error: {}",errors);

		return ResponseEntity.badRequest().body(ApiResponse.error(INVALID_REQUEST, errors));
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<ApiResponse> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e){

		// 모든 유효성 검사 에러 메시지 저장
		Map<String, String> errors = new HashMap<>();
    	errors.put("error", e.getMessage());

		log.error("InvalidDataAccessApiUsageException error: {}",errors);

		return ResponseEntity.badRequest().body(ApiResponse.error(INVALID_REQUEST, errors));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException e){
		log.error("{} is BadCredentialsException occurred.", e.getMessage());

		return ResponseEntity.badRequest().body(ApiResponse.error(INVALID_EMAIL_OR_PASSWORD));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleException(Exception e){
		log.error("{} is Exception occurred.", e.getMessage());

		Map<String, String> errors = new HashMap<>();
    	errors.put("error", e.getMessage());

		return ResponseEntity.internalServerError().body(ApiResponse.error(INTERVAL_SERVER_ERROR, errors));
	}

}
