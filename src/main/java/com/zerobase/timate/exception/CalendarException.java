package com.zerobase.timate.exception;

import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.type.ErrorCode;
import java.util.List;
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
	private List<UserDto.Response> members;

	public CalendarException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getMessage();
	}

    public CalendarException(ErrorCode errorCode, List<UserDto.Response> members) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
        this.members = members;
    }

}
