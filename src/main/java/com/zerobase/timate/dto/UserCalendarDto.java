package com.zerobase.timate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.UserCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserCalendarDto {

	public interface ChangeMaster {}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(name = "UserCalendarRequestDto", description = "사용자 캘린더 요청 DTO")
	public static class Request {

		private Long calendarId;
		private Long userId;
		@NotNull(groups = ChangeMaster.class)
		private Long newMasterId;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {

		@JsonProperty("calendar_id")
		private Long calendarId;
		@JsonProperty("calendar_name")
		private String calendarName;
		@JsonProperty("user_id")
		private Long userId;
		@JsonProperty("user_name")
		private String userName;
		@JsonProperty("role")
		private MemberRole role;

		public static Response from(UserCalendar uc) {
			return Response.builder()
				.calendarId(uc.getCalendar().getId())
				.calendarName(uc.getCalendar().getName())
				.userId(uc.getUser().getId())
				.userName(uc.getUser().getName())
				.role(uc.getRole())
				.build();
		}

	}

}
