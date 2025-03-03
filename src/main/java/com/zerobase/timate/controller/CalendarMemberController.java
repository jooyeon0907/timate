package com.zerobase.timate.controller;


import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.UserCalendarDto;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.service.CalendarMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar/member")
@RequiredArgsConstructor
public class CalendarMemberController {

	private final CalendarMemberService calendarMemberService;
	private final AuthService authService;


	// TODO: 초대 링크 생성

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> create(@RequestBody UserCalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		return ResponseEntity.ok(ApiResponse.success(calendarMemberService.create(request)));
	}

	@GetMapping("/list")
	public ResponseEntity<ApiResponse> list(@RequestParam Long calendarId) {
		return ResponseEntity.ok(ApiResponse.success(calendarMemberService.memberList(authService.getAuthenticatedUserId(), calendarId)));
	}

	@PostMapping("/exit")
	public ResponseEntity<ApiResponse> exit(@RequestBody UserCalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		calendarMemberService.exit(request);
		return ResponseEntity.ok(ApiResponse.success("캘린더에 퇴장하였습니다."));
	}

	@PostMapping("/change-master")
	public ResponseEntity<ApiResponse> changeMaster(@RequestBody UserCalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		calendarMemberService.changeMasterAndExit(request);
		return ResponseEntity.ok(ApiResponse.success(""));
	}

}
