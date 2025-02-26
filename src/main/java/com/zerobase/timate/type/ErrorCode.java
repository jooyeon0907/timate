package com.zerobase.timate.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INTERVAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
	INVALID_REQUEST("잘못된 요청입니다."),
	FAILED_SEND_EMAIL("이메일 전송을 실패하였습니다."),
	ALREADY_USER("이미 가입된 회원입니다."),
	FAILED_AUTH("인증이 실패되었습니다."),
	ALREADY_AUTH("이미 인증된 회원입니다."),
	INVALID_EMAIL_OR_PASSWORD("잘못된 이메일 또는 비밀번호입니다."),
	USER_NOT_FOUND("회원 정보가 존재하지 않습니다.")
	;

	private final String message;
}
