package com.zerobase.timate.controller;


import static com.zerobase.timate.type.SuccessCode.CALENDAR_EXIT_SUCCESS;
import static com.zerobase.timate.type.SuccessCode.CALENDAR_INVITED;
import static com.zerobase.timate.type.SuccessCode.MASTER_ROLE_TRANSFER_AND_EXIT_SUCCESS;

import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.UserCalendarDto;
import com.zerobase.timate.dto.UserCalendarDto.ChangeMaster;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.service.CalendarMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendars/{calendarId}/members")
@RequiredArgsConstructor
public class CalendarMemberController {

	private final CalendarMemberService calendarMemberService;
	private final AuthService authService;


	@PostMapping
	public ResponseEntity<ApiResponse> addMember(@PathVariable Long calendarId) {
		return ResponseEntity.ok(ApiResponse.success(calendarMemberService.addMember(authService.getAuthenticatedUserId(), calendarId)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse> listMembers(@PathVariable Long calendarId) {
		return ResponseEntity.ok(ApiResponse.success(calendarMemberService.memberList(authService.getAuthenticatedUserId(), calendarId)));
	}

	@DeleteMapping("/exit")
	public ResponseEntity<ApiResponse> exit(@PathVariable Long calendarId) {
		calendarMemberService.exit(authService.getAuthenticatedUserId(), calendarId);
		return ResponseEntity.ok(ApiResponse.success(CALENDAR_EXIT_SUCCESS));
	}

	@PostMapping("/transfer-master")
	public ResponseEntity<ApiResponse> transferMasterAndExit(@PathVariable Long calendarId,
															@Validated(ChangeMaster.class)
															@RequestBody UserCalendarDto.Request request) {
		request.setCalendarId(calendarId);
		request.setUserId(authService.getAuthenticatedUserId());
		calendarMemberService.transferMasterAndExit(request);
		return ResponseEntity.ok(ApiResponse.success(MASTER_ROLE_TRANSFER_AND_EXIT_SUCCESS));
	}


	@GetMapping("/invitation-link")
	public ResponseEntity<String> generateInviteLink(@PathVariable Long calendarId) {
		return ResponseEntity.ok(calendarMemberService.generateInviteLink(
			authService.getAuthenticatedUserId(), calendarId));
	}

	@GetMapping("/invitation")
	public ResponseEntity<ApiResponse> acceptInvitation(HttpServletRequest request) {
		calendarMemberService.acceptInvitation(authService.getAuthenticatedUserId(), request.getParameter("code"));
		return ResponseEntity.ok(ApiResponse.success(CALENDAR_INVITED));
	}

}
