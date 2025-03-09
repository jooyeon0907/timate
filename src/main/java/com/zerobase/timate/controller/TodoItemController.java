package com.zerobase.timate.controller;


import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.TodoItemDto;
import com.zerobase.timate.service.AuthService;
import com.zerobase.timate.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendars/{calendarId}/schedules/{scheduleId}/todo-items")
@RequiredArgsConstructor
public class TodoItemController {

	private final TodoItemService todoItemService;
	private final AuthService authService;

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateTodoStatus(
											@PathVariable Long calendarId,
											@PathVariable Long scheduleId,
											@PathVariable Long id,
											@RequestBody TodoItemDto.Request request) {
		request.setUserId(authService.getAuthenticatedUserId());
		request.setCalendarId(calendarId);
		request.setScheduleId(scheduleId);
		request.setId(id);
		return ResponseEntity.ok(ApiResponse.success(todoItemService.updateTodoStatus(request)));
	}

}
