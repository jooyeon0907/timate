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
	@Getter
	@Setter
	@AllArgsConstructor
	public static class Request {

		@NotNull
		private Long id;

		@NotNull(message = "이름은 필수 항목입니다.")
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
