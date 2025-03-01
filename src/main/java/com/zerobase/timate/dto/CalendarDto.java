package com.zerobase.timate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.CalendarType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class CalendarDto {

	// 캘린더 생성과 수정 요청을 구분할 수 있도록 그룹 인터페이스 생성
	// 필드별로 Validation Group 설정 -> Controller 에 @Validated 에 그룹 적용
	public interface ValidationGroups {
		interface Create {}  // 캘린더 생성 그룹
		interface Update {}  // 캘린더 수정 그룹
		interface Delete {}  // 캘린더 삭제 그룹
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@Schema(name = "CalendarRequestDto", description = "일정 요청 DTO")
	public static class Request {

		@NotNull(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class, ValidationGroups.Delete.class})
		private Long id;
		private Long userId;
		@NotNull(message = "이름은 필수 항목입니다.", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
		private String name;
		@NotNull(groups = ValidationGroups.Create.class)
		private CalendarType type;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {

		@JsonProperty("id")
		private Long id;
		@JsonProperty("name")
		private String name;
		@JsonProperty("type")
		private CalendarType type;

		public static Response from(Calendar calendar) {
			return Response.builder()
				.id(calendar.getId())
				.type(calendar.getType())
				.name(calendar.getName())
				.build();
		}

	}

}
