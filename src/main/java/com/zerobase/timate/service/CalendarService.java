package com.zerobase.timate.service;

import static com.zerobase.timate.dto.CalendarDto.Cached.toEntity;

import com.zerobase.timate.dto.CalendarDto;
import com.zerobase.timate.dto.CalendarDto.Request;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.repository.CalendarRepository;
import com.zerobase.timate.repository.UserCalendarRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
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

		return CalendarDto.Response.from(
			toEntity(commonService.getCalendarFromCache(user.getId(), calendar.getId())));
	}

	public List<CalendarDto.Response> list(Long userId) {
		List<UserCalendar> userCalendars = userCalendarRepository.findUserCalendarsWithCalendars(userId);

		return userCalendars.stream()
			.map(userCalendar -> CalendarDto.Response.from(userCalendar.getCalendar()))
			.collect(Collectors.toList());
	}

	public CalendarDto.Response read(Long userId, Long id) {
		return CalendarDto.Response.from(
			toEntity(commonService.getCalendarFromCache(userId, id)));
	}

	public CalendarDto.Response update(Request request) {
		commonService.validateCalendarMaster(request.getUserId(), request.getId());
		return CalendarDto.Response.from(toEntity(commonService.updateCalendarFromCache(request)));
	}

	@Transactional
	@CacheEvict(value = "calendar", key = "#userId + ':' + #id")
	public void delete(Long userId, Long id) {
		commonService.validateCalendarMaster(userId, id);

		Calendar calendar = toEntity(commonService.getCalendarFromCache(userId, id));

		userCalendarRepository.deleteByCalendarId(calendar.getId());

		calendarRepository.deleteById(calendar.getId());

	}


}
