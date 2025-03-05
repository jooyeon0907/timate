package com.zerobase.timate.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    SIGNUP_SUCCESS("회원가입이 완료되었습니다."),
    EMAIL_AUTH_SUCCESS("이메일 인증이 완료되었습니다."),
    LOGOUT_SUCCESS("로그아웃 성공하였습니다."),
	DELETE_SUCCESS("삭제되었습니다.")
	;

	private final String message;
}
