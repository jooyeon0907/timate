package com.zerobase.timate.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataInsertController {

	private final DataInsertService dataInsertService;

	@PostMapping("/insert/calendars")
	public ResponseEntity<String> insertCalendars(@RequestParam int count, @RequestParam Long userId) {
		dataInsertService.insertCalendars(count, userId);
		return ResponseEntity.ok(count + "개의 캘린더와 UserCalendar가 추가되었습니다.");
	}

}