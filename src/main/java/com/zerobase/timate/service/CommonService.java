package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.NOT_CALENDAR_MASTER;
import static com.zerobase.timate.type.ErrorCode.NOT_CALENDAR_MEMBER;
import static com.zerobase.timate.type.ErrorCode.USER_NOT_FOUND;

import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.exception.AuthException;
import com.zerobase.timate.exception.CalendarException;
import com.zerobase.timate.repository.UserCalendarRepository;
import com.zerobase.timate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

	private final UserRepository userRepository;
	private final UserCalendarRepository userCalendarRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));
	}

	public UserCalendar getUserCalendar(Long userId, Long calendarId) {
		return userCalendarRepository.findByUserIdAndCalendarId(userId, calendarId)
			.orElseThrow(() -> new CalendarException(NOT_CALENDAR_MEMBER));
	}

	public void checkCalendarMaster(Long userId, Long calendarId) {
		UserCalendar userCalendar = getUserCalendar(userId, calendarId);
		 if (!userCalendar.getRole().equals(MemberRole.MASTER)) {
			 throw new CalendarException(NOT_CALENDAR_MASTER);
		 }
	}


}
