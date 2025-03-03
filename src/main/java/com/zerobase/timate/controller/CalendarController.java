package com.zerobase.timate.controller;


import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.CalendarDto;
import com.zerobase.timate.dto.CalendarDto.ValidationGroups;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;
	private final AuthService authService;

//    - 캘린더 나가기


	@PostMapping("/create")
	public ResponseEntity<ApiResponse> create(@Validated(ValidationGroups.Create.class)
												@RequestBody CalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		return ResponseEntity.ok(ApiResponse.success(calendarService.create(request)));
	}

	@GetMapping("/list")
	public ResponseEntity<ApiResponse> list() {
		return ResponseEntity.ok(ApiResponse.success(calendarService.list(authService.getAuthenticatedUserId())));
	}

	@GetMapping("/read")
	public ResponseEntity<ApiResponse> read(@RequestParam Long id) {
		return ResponseEntity.ok(ApiResponse.success(calendarService.read(id, authService.getAuthenticatedUserId())));
	}

	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(@Validated(ValidationGroups.Update.class)
												@RequestBody CalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		return ResponseEntity.ok(ApiResponse.success(calendarService.update(request)));
	}

	@PostMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@Validated(ValidationGroups.Delete.class)
												@RequestBody CalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		calendarService.delete(request);
		return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
	}




}
