package com.zerobase.timate.dto;

import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

		private Long id;
		private Long creatorId;
		private Long calendarId;
		private String title;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private String memo;

		private String placeName;
		private String address;
		private double latitude;
		private double longitude;

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

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Cached {

		private Long id;
		private Long creatorId;
		private String title;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private String memo;

		private String placeName;
		private String address;
		private double latitude;
		private double longitude;

		private Calendar calendar;

		public static Cached from(Schedule schedule) {
			return Cached.builder()
				.id(schedule.getId())
				.creatorId(schedule.getCreatorId())
				.calendar(schedule.getCalendar())
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

	  public static Schedule toEntity(ScheduleDto.Cached dto) {
		return Schedule.builder()
			.id(dto.getId())
			.creatorId(dto.getCreatorId())
			.calendar(dto.getCalendar())
			.title(dto.getTitle())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.memo(dto.getMemo())
			.build();
	  }


	}

}
