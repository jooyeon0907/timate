package com.zerobase.timate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.timate.entity.Schedule;
import com.zerobase.timate.entity.TodoItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
public class TodoItemDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@Schema(name = "TodoItemRequestDto", description = "투두 항목 요청 DTO")
	public static class Request {
		private Long id;
		private Long userId;
		private Long calendarId;
		private Long scheduleId;

		private String task;
		private boolean isCompleted;
		//	private Schedule schedule;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {

		private Long id;
		private String task;
		private boolean isCompleted;

		public static Response from(TodoItem todoItem) {
			return Response.builder()
				.id(todoItem.getId())
				.task(todoItem.getTask())
				.isCompleted(todoItem.isCompleted())
				.build();
		}

	}

}
