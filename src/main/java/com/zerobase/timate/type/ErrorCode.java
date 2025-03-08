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
	FAILED_EMAIL_AUTH("이메일 인증이 실패되었습니다."),
	INVALID_USER_TOKEN("로그인 정보가 유효하지 않습니다."),
	ALREADY_EMAIL_AUTH("이미 인증된 회원입니다."),
	INVALID_EMAIL_OR_PASSWORD("잘못된 이메일 또는 비밀번호입니다."),
	USER_NOT_FOUND("회원 정보가 존재하지 않습니다."),

	CALENDAR_NOT_FOUND("캘린더 정보가 존재하지 않습니다."),
	EXISTS_CALENDAR_MEMBER("해당 캘린더에 이미 초대된 멤버입니다."),
    MEMBER_LIST_REQUIRED("새로운 캘린더 관리자를 선택해야 합니다."),
    SELF_PERMISSION_TRANSFER("본인에게 권한 양도를 할 수 없습니다."),
	CALENDAR_DELETION_REQUIRED("캘린더에 한 명만 남아있는 경우, 해당 캘린더는 삭제됩니다."),
	NOT_CALENDAR_MEMBER("해당 사용자는 이 캘린더의 멤버가 아닙니다."),
	NOT_CALENDAR_MASTER("해당 사용자는 이 캘린더의 관리자가 아닙니다."),
	CANNOT_INVITE_TO_PERSONAL_CALENDAR("개인용 캘린더는 멤버를 초대할 수 없습니다."),
	INVALID_INVITATION_LINK("초대 링크가 만료되었거나 존재하지 않습니다."),

	SCHEDULE_NOT_FOUND("일정 정보가 존재하지 않습니다."),
	INVALID_PERIOD("period는 monthly, weekly, 또는 daily 중 하나여야 합니다.")
	;

	private final String message;
}
