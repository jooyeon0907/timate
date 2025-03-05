package com.zerobase.timate.controller;


import static com.zerobase.timate.type.SuccessCode.DELETE_SUCCESS;

import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.CalendarDto;
import com.zerobase.timate.dto.CalendarDto.ValidationGroups;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendars")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;
	private final AuthService authService;

	@PostMapping
	public ResponseEntity<ApiResponse> create(@Validated(ValidationGroups.Create.class)
												@RequestBody CalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		return ResponseEntity.ok(ApiResponse.success(calendarService.create(request)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse> list() {
		Long userId = authService.getAuthenticatedUserId();
		return ResponseEntity.ok(ApiResponse.success(calendarService.list(userId)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> read(@PathVariable Long id) {
		Long userId = authService.getAuthenticatedUserId();
		return ResponseEntity.ok(ApiResponse.success(calendarService.read(userId, id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> update(@PathVariable Long id,
												@Validated(ValidationGroups.Update.class)
												@RequestBody CalendarDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		request.setId(id);
		return ResponseEntity.ok(ApiResponse.success(calendarService.update(request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
		Long userId = authService.getAuthenticatedUserId();
		calendarService.delete(userId, id);
		return ResponseEntity.ok(ApiResponse.success(DELETE_SUCCESS));
	}

}
