package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.CALENDAR_NOT_FOUND;

import com.zerobase.timate.dto.CalendarDto;
import com.zerobase.timate.dto.CalendarDto.Request;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.entity.UserCalendarId;
import com.zerobase.timate.exception.CalendarException;
import com.zerobase.timate.repository.CalendarRepository;
import com.zerobase.timate.repository.UserCalendarRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CalendarService {

	private final CommonService commonService;

	private final CalendarRepository calendarRepository;
	private final UserCalendarRepository userCalendarRepository;

	@Transactional
	public CalendarDto.Response create(Request request) {
		User user = commonService.getUserById(request.getUserId());

		Calendar calendar = Calendar.builder()
			.name(request.getName())
			.type(request.getType())
			.build();
		calendarRepository.save(calendar);

		UserCalendarId userCalendarId = new UserCalendarId(user.getId(), calendar.getId());

		UserCalendar userCalendar = new UserCalendar().builder()
			.id(userCalendarId) // 복합키를 명시적으로 설정
			.user(user)
			.calendar(calendar)
			.role(MemberRole.MASTER)
			.build();
		userCalendarRepository.save(userCalendar);

		return CalendarDto.Response.from(calendar);
	}

	public List<CalendarDto.Response> list(Long userId) {

		List<UserCalendar> userCalendars = userCalendarRepository.findByUserId(userId);

		// 각 UserCalendar에서 calendarId를 기반으로 Calendar 객체를 찾아서 리스트로 반환
		return userCalendars.stream()
			.map(userCalendar -> CalendarDto.Response.from(userCalendar.getCalendar()))
			.collect(Collectors.toList());
	}

	public CalendarDto.Response read(Long id, Long userId) {
		Calendar calendar = getCalendarById(id);
		return CalendarDto.Response.from(calendar);
	}

	public CalendarDto.Response update(Request request) {
		Calendar calendar = getCalendarById(request.getCalendarId());
		calendar.setName(request.getName());
		calendarRepository.save(calendar);

		return CalendarDto.Response.from(calendar);
	}

	@Transactional
	public void delete(Request request) {
		Calendar calendar = getCalendarById(request.getCalendarId());

		// 해당 유저 권한이 MASTER 인지 확인
		commonService.checkCalendarMaster(request.getUserId(), calendar.getId());

		// userCalendar 삭제
		userCalendarRepository.deleteByCalendarId(calendar.getId());

		calendarRepository.deleteById(calendar.getId());

	}

	public Calendar getCalendarById(Long id) {
		return calendarRepository.findById(id)
			.orElseThrow(() -> new CalendarException(CALENDAR_NOT_FOUND));
	}


}
