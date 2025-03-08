package com.zerobase.timate.controller;


import static com.zerobase.timate.type.SuccessCode.DELETE_SUCCESS;

import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.ScheduleDto;
import com.zerobase.timate.dto.ScheduleDto.ValidationGroups;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.service.ScheduleService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendars/{calendarId}/schedules")
@RequiredArgsConstructor
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final AuthService authService;

	@PostMapping
	public ResponseEntity<ApiResponse> create(
											@PathVariable Long calendarId,
											@Validated(ValidationGroups.Create.class)
											@RequestBody ScheduleDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		request.setCalendarId(calendarId);
		return ResponseEntity.ok(ApiResponse.success(scheduleService.create(request)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse> list(
											@PathVariable Long calendarId,
											@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
											@RequestParam String period) {
		Long userId = authService.getAuthenticatedUserId();
		return ResponseEntity.ok(
			ApiResponse.success(scheduleService.getSchedulesByPeriod(userId, calendarId, date, period)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> read(@PathVariable Long calendarId, @PathVariable Long id) {
		Long userId = authService.getAuthenticatedUserId();
		return ResponseEntity.ok(ApiResponse.success(scheduleService.read(userId, calendarId, id)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> update(
												@PathVariable Long calendarId,
												@PathVariable Long id,
												@Validated(ValidationGroups.Common.class)
												@RequestBody ScheduleDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		request.setCalendarId(calendarId);
		request.setId(id);
		return ResponseEntity.ok(ApiResponse.success(scheduleService.update(request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long calendarId, @PathVariable Long id) {
		Long userId = authService.getAuthenticatedUserId();
		scheduleService.delete(userId, calendarId, id);
		return ResponseEntity.ok(ApiResponse.success(DELETE_SUCCESS));
	}

}
