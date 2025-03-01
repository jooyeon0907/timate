package com.zerobase.timate.controller;


import com.zerobase.timate.dto.ApiResponse;
import com.zerobase.timate.dto.CalendarDto;
import com.zerobase.timate.dto.CalendarDto.ValidationGroups;
import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.service.CalendarService;
import com.zerobase.timate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;


//	  - 캘린더 생성
//	  - 캘린더 조회
//    - 캘린더 목록 조회
//    - 캘린더 수정
//    - 캘린더 삭제


//    - 캘린더 멤버추가
//    - 캘린더 멤버 목록 조회
//    - 캘린더 나가기
//    - 생성자 권한 변경


	@PostMapping("/create")
	public ResponseEntity<ApiResponse> create(@Validated(ValidationGroups.Create.class)
												@RequestBody CalendarDto.Request request) {
		return ResponseEntity.ok(ApiResponse.success(calendarService.create(request)));
	}

	@GetMapping("/list")
	public ResponseEntity<ApiResponse> list(@RequestParam Long userId) {
		return ResponseEntity.ok(ApiResponse.success(calendarService.list(userId)));
	}

	@GetMapping("/read")
	public ResponseEntity<ApiResponse> read(@RequestParam Long id, @RequestParam Long userId) {
		return ResponseEntity.ok(ApiResponse.success(calendarService.read(id, userId)));
	}

	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(@Validated(ValidationGroups.Update.class)
												@RequestBody CalendarDto.Request request) {
		return ResponseEntity.ok(ApiResponse.success(calendarService.update(request)));
	}

	@PostMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@Validated(ValidationGroups.Delete.class)
												@RequestBody CalendarDto.Request request) {
		calendarService.delete(request);
		return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
	}

}
