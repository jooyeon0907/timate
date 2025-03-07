package com.zerobase.timate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.timate.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

	public interface UpdateGroup {}  // 수정 시 사용될 그룹

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Request {
		private Long id;
		@NotNull(message = "이름은 필수 항목입니다.", groups = {UpdateGroup.class})
		private String name;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {

		@JsonProperty("id")
		private Long id;
		@JsonProperty("email")
		private String email;
		@JsonProperty("name")
		private String name;

		public static Response from(User user) {
			return Response.builder()
				.id(user.getId())
				.email(user.getEmail())
				.name(user.getName())
				.build();
		}

	}

}
