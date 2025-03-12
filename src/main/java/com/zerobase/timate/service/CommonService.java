package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.CALENDAR_NOT_FOUND;
import static com.zerobase.timate.type.ErrorCode.NOT_CALENDAR_MASTER;
import static com.zerobase.timate.type.ErrorCode.NOT_CALENDAR_MEMBER;
import static com.zerobase.timate.type.ErrorCode.SCHEDULE_NOT_FOUND;
import static com.zerobase.timate.type.ErrorCode.USER_NOT_FOUND;

import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.Schedule;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.exception.AuthException;
import com.zerobase.timate.exception.CalendarException;
import com.zerobase.timate.exception.ScheduleException;
import com.zerobase.timate.repository.CalendarRepository;
import com.zerobase.timate.repository.ScheduleRepository;
import com.zerobase.timate.repository.UserCalendarRepository;
import com.zerobase.timate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

	private final UserRepository userRepository;
	private final UserCalendarRepository userCalendarRepository;

	private final CalendarRepository calendarRepository;
	private final ScheduleRepository scheduleRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));
	}

	public Calendar getCalendarById(Long calendarId) {
		return calendarRepository.findById(calendarId)
			.orElseThrow(() -> new CalendarException(CALENDAR_NOT_FOUND));
	}

	@Cacheable(value = "calendar", key = "#userId + ':' + #calendarId")
	public Calendar getCalendar(Long userId, Long calendarId) {
		log.info("Calling getCalendar method for userId: {}, calendarId: {}", userId, calendarId);
		validateCalendarMember(userId, calendarId);
		return calendarRepository.findById(calendarId)
			.orElseThrow(() -> new CalendarException(CALENDAR_NOT_FOUND));
	}

	/**
	 * 주어진 userId와 calendarId에 해당하는 UserCalendar를 반환합니다.
	 * <p>
	 * 이 메소드는 `validateCalendarMember` 메소드를 사용하여 먼저 사용자가 해당 캘린더의 멤버인지 확인한 후, 멤버라면 `UserCalendar`
	 * 객체를 반환합니다.
	 * <p>
	 * 이렇게 메소드를 분리한 이유는 **유지보수성**을 위해서입니다. `validateCalendarMember`는 사용자가 캘린더의 멤버인지 확인하는 독립적인
	 * 로직이므로, 여러 곳에서 재사용 가능하도록 분리되었습니다.
	 *
	 * @param userId     사용자의 ID
	 * @param calendarId 캘린더의 ID
	 * @return UserCalendar 해당 사용자의 캘린더 정보
	 * @throws CalendarException 사용자가 캘린더의 멤버가 아닐 경우 예외 발생
	 */
	public UserCalendar getUserCalendar(Long userId, Long calendarId) {
		validateCalendarMember(userId, calendarId);
		return userCalendarRepository.findByUserIdAndCalendarId(userId, calendarId)
			.orElseThrow(() -> new CalendarException(CALENDAR_NOT_FOUND));
	}

	@Cacheable(value = "schedule", key = "#calendarId + ':' + #scheduleId")
	public Schedule getSchedule(Long userId, Long calendarId, Long scheduleId) {
		log.info("Calling getSchedule method for calendarId: {}, scheduleId: {}", calendarId, scheduleId);
		validateCalendarMember(userId, calendarId);
		validateSchedule(calendarId, scheduleId);
		return scheduleRepository.findByIdAndCalendarId(scheduleId, calendarId)
			.orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
	}

	/**
	 * 주어진 userId와 calendarId에 대해 사용자가 해당 캘린더의 멤버인지 확인합니다.
	 * <p>
	 * 이 메소드는 `isUserCalendarMember`를 호출하여 캘린더 멤버 여부를 확인하고, 멤버가 아니면 예외를 발생시킵니다.
	 * <p>
	 * **유지보수성**을 고려하여 이 메소드를 별도로 분리하였습니다. 이 메소드는 `getUserCalendar`와 같은 다른 메소드에서 재사용이 가능하며, 코드 중복을
	 * 피하고, 멤버 여부를 확인하는 로직을 독립적으로 관리할 수 있습니다.
	 *
	 * @param userId     사용자의 ID
	 * @param calendarId 캘린더의 ID
	 * @throws CalendarException 사용자가 캘린더의 멤버가 아니면 예외 발생
	 */
	public void validateCalendarMember(Long userId, Long calendarId) {
		log.info("Calling validateCalendarMember method for userId: {}, calendarId: {}", userId, calendarId);
		if (!isCalendarMember(userId, calendarId)) {
			throw new CalendarException(NOT_CALENDAR_MEMBER);
		}
	}

	public void validateSchedule(Long calendarId, Long scheduleId) {
		if (!isScheduleCalendar(calendarId, scheduleId)) {
			throw new ScheduleException(SCHEDULE_NOT_FOUND);
		}
	}

	/**
	 * 주어진 userId와 calendarId에 대해 사용자가 해당 캘린더의 멤버인지 여부를 확인합니다.
	 * <p>
	 * 이 메소드는 캘린더의 멤버 여부를 `true` 또는 `false`로 반환합니다.
	 * <p>
	 * `validateCalendarMember`와 같은 다른 메소드에서 호출되어 재사용될 수 있도록 별도로 분리되었습니다. **재사용성**을 높이기 위해, 이 메소드는
	 * 멤버 여부만 단순히 반환하므로 독립적으로 사용될 수 있습니다.
	 *
	 * @param userId     사용자의 ID
	 * @param calendarId 캘린더의 ID
	 * @return boolean 사용자가 캘린더의 멤버이면 true, 아니면 false
	 */
	public boolean isCalendarMember(Long userId, Long calendarId) {
		log.info("Calling isCalendarMember method for userId: {}, calendarId: {}", userId, calendarId);
		return userCalendarRepository.existsByUserIdAndCalendarId(userId, calendarId);
	}

	public boolean isScheduleCalendar(Long calendarId, Long scheduleId) {
		return scheduleRepository.existsByIdAndCalendarId(scheduleId, calendarId);
	}

	public void validateCalendarMaster(Long userId, Long calendarId) {
		UserCalendar userCalendar = getUserCalendar(userId, calendarId);
		if (userCalendar.getRole() != MemberRole.MASTER) {
			throw new CalendarException(NOT_CALENDAR_MASTER);
		}
	}

}
