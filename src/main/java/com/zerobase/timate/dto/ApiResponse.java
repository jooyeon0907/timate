package com.zerobase.timate.dto;

import com.zerobase.timate.type.ErrorCode;
import com.zerobase.timate.type.SuccessCode;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private final String code;
	private final String message;
	private final T data;
    private Map<String, String> errors; // 유효성 검사 에러 필드

	public ApiResponse(String code, String message, T data, Map<String, String> errors) {
		this.code = code;
		this.message = message;
		this.data = data;
        this.errors = errors != null ? errors : Collections.emptyMap();
	}


	public static <T> ApiResponse<T> success(SuccessCode successCode) {
		return new ApiResponse<>(successCode.name(), successCode.getMessage(), null, null);
	}

	public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
		return new ApiResponse<>(successCode.name(), successCode.getMessage(), data, null);
	}

	public static <T> ApiResponse<T> error(ErrorCode errorCode) {
		return new ApiResponse<>(errorCode.name(), errorCode.getMessage(), null, null);
	}

	public static <T> ApiResponse<T> error(ErrorCode errorCode, Map<String, String> errors) {
		return new ApiResponse<>(errorCode.name(), errorCode.getMessage(), null, errors);
	}

}