package com.zerobase.timate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "유효한 이메일 주소를 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호 필수 항목입니다.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다. ")
	private String password;

	@NotBlank(message = "이름은 필수 항목입니다.")
	private String name;
}
