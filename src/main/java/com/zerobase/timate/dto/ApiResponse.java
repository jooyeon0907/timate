package com.zerobase.timate.dto;

import com.zerobase.timate.type.ErrorCode;
import com.zerobase.timate.type.SuccessCode;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class ApiResponse {

	private String code;
	private String message;
    private final Map<String, String> errors;  // 유효성 검사 에러 필드

    private ApiResponse(String code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors != null ? errors : Collections.emptyMap();
    }

	public static ApiResponse success(SuccessCode successCode) {
		return new ApiResponse(successCode.name(), successCode.getMessage(), Collections.emptyMap());
	}

	public static ApiResponse error(ErrorCode errorCode) {
		return new ApiResponse(errorCode.name(), errorCode.getMessage(), Collections.emptyMap());
	}

    public static ApiResponse error(ErrorCode errorCode, Map<String, String> errors) {
        return new ApiResponse(errorCode.name(), errorCode.getMessage(), errors);
    }


}