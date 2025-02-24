package com.zerobase.timate.exception;

import static com.zerobase.timate.type.ErrorCode.INTERVAL_SERVER_ERROR;
import static com.zerobase.timate.type.ErrorCode.INVALID_REQUEST;

import com.zerobase.timate.dto.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ApiResponse handleAccountException(AuthException e){
		log.error("{} is AuthException occurred.", e.getErrorMessage());

		return ApiResponse.error(e.getErrorCode());
	}

	// DTO 유효성 검사 실패 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse MethodArgumentNotValidException(MethodArgumentNotValidException e){
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> errors = new HashMap<>();

		// 모든 유효성 검사 에러 메시지 저장
		bindingResult.getFieldErrors().forEach( error ->
			errors.put(error.getField(), error.getDefaultMessage()));


		log.error("Validation error: {}",errors);

		return ApiResponse.error(INVALID_REQUEST, errors);
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse handleException(Exception e){
		log.error("{} is Exception occurred.", e.getMessage());

		return ApiResponse.error(INTERVAL_SERVER_ERROR);
	}

}
