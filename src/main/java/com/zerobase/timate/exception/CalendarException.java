package com.zerobase.timate.exception;

import com.zerobase.timate.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CalendarException extends RuntimeException{
	private ErrorCode errorCode;
	private String errorMessage;

	public CalendarException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getMessage();
	}
}
