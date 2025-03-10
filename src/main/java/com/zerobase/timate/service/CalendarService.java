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

		Calendar calendar = Calendar.of(request);

		calendarRepository.save(calendar);

		UserCalendar userCalendar = UserCalendar.of(user, calendar, MemberRole.MASTER);
		userCalendarRepository.save(userCalendar);

		return CalendarDto.Response.from(calendar);
	}

	public List<CalendarDto.Response> list(Long userId) {
		List<UserCalendar> userCalendars = userCalendarRepository.findUserCalendarsWithCalendars(userId);

		return userCalendars.stream()
			.map(userCalendar -> CalendarDto.Response.from(userCalendar.getCalendar()))
			.collect(Collectors.toList());
	}

	public CalendarDto.Response read(Long userId, Long id) {
		commonService.checkCalendarMember(userId, id);
		return CalendarDto.Response.from(getCalendarById(id));
	}

	public CalendarDto.Response update(Request request) {
		commonService.checkCalendarMaster(request.getUserId(), request.getId());

		Calendar calendar = getCalendarById(request.getId());
		calendar.setName(request.getName());
		calendarRepository.save(calendar);

		return CalendarDto.Response.from(calendar);
	}

	@Transactional
	public void delete(Long useId, Long id) {
		commonService.checkCalendarMaster(useId, id);

		Calendar calendar = getCalendarById(id);

		userCalendarRepository.deleteByCalendarId(calendar.getId());

		calendarRepository.deleteById(calendar.getId());

	}

	public Calendar getCalendarById(Long id) {
		return calendarRepository.findById(id)
			.orElseThrow(() -> new CalendarException(CALENDAR_NOT_FOUND));
	}


}
