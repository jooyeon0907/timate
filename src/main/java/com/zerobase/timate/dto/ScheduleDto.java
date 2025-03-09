package com.zerobase.timate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.timate.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ScheduleDto {

	public interface ValidationGroups {
		interface Create {}
		interface Common {}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@Schema(name = "ScheduleRequestDto", description = "일정 요청 DTO")
	public static class Request {

		private Long id;
		private Long userId;
		private Long calendarId;

		@NotNull(message = "제목은 필수 항목입니다.", groups = ValidationGroups.Create.class)
		private String title;
		@NotNull(message = "시작일은 필수 항목입니다.", groups = ValidationGroups.Create.class)
		private LocalDateTime startDate;
		@NotNull(message = "종료일은 필수 항목입니다.", groups = ValidationGroups.Create.class)
		private LocalDateTime endDate;

		private String memo;

		private Long googleEventId;

		// 장소 관련
		private String placeName;
		private String address;
		private double latitude;
		private double longitude;

		private List<TodoItemDto.Response> todoItems;

		@AssertTrue(message = "종료 날짜를 시작 날짜 이전으로 설정할 수 없습니다.",
					groups = {ValidationGroups.Create.class, ValidationGroups.Common.class})
		public boolean isEndDateValid() {
			if (startDate == null || endDate == null) return true;
			return !startDate.isAfter(endDate);
		}

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {

		@JsonProperty("id")
		private Long id;
		@JsonProperty("creator_id")
		private Long creatorId;
		@JsonProperty("calendar_id")
		private Long calendarId;
		@JsonProperty("title")
		private String title;
		@JsonProperty("start_date")
		private LocalDateTime startDate;
		@JsonProperty("end_date")
		private LocalDateTime endDate;
		@JsonProperty("memo")
		private String memo;

		@JsonProperty("place_name")
		private String placeName;
		@JsonProperty("address")
		private String address;
		@JsonProperty("latitude")
		private double latitude;
		@JsonProperty("longitude")
		private double longitude;

		private List<TodoItemDto.Response> todoItems;

		public static Response from(Schedule schedule) {
			return Response.builder()
				.id(schedule.getId())
				.creatorId(schedule.getCreatorId())
				.calendarId(schedule.getCalendar().getId())
				.title(schedule.getTitle())
				.startDate(schedule.getStartDate())
				.endDate(schedule.getEndDate())
				.memo(schedule.getMemo())
				.placeName(schedule.getPlaceName())
				.address(schedule.getAddress())
				.latitude(schedule.getLatitude())
				.longitude(schedule.getLongitude())
				.build();
		}

		public static Response withTodoFrom(Schedule schedule) {
			List<TodoItemDto.Response> todoItems = Optional.ofNullable(schedule.getTodoItems())
				.orElse(Collections.emptyList()) // null이면 빈 리스트 반환
				.stream()
				.map(TodoItemDto.Response::from)
				.collect(Collectors.toList());

			return Response.builder()
				.id(schedule.getId())
				.creatorId(schedule.getCreatorId())
				.calendarId(schedule.getCalendar().getId())
				.title(schedule.getTitle())
				.startDate(schedule.getStartDate())
				.endDate(schedule.getEndDate())
				.memo(schedule.getMemo())
				.placeName(schedule.getPlaceName())
				.address(schedule.getAddress())
				.latitude(schedule.getLatitude())
				.longitude(schedule.getLongitude())
				.todoItems(todoItems)
				.build();
		}
	}

}
