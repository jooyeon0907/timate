package com.zerobase.timate.service;

import static com.zerobase.timate.entity.PeriodType.DAILY;
import static com.zerobase.timate.type.ErrorCode.INVALID_PERIOD;
import static com.zerobase.timate.type.ErrorCode.SCHEDULE_NOT_FOUND;

import com.zerobase.timate.dto.ScheduleDto;
import com.zerobase.timate.dto.ScheduleDto.Response;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.PeriodType;
import com.zerobase.timate.entity.Schedule;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.exception.ScheduleException;
import com.zerobase.timate.repository.ScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ScheduleService {

	private final CommonService commonService;

	private final ScheduleRepository scheduleRepository;

	private final RedisTemplate<String, Object> redisTemplate;

	@Transactional
	public ScheduleDto.Response create(ScheduleDto.Request request) {
		Long userId = request.getUserId();
		Long calendarId = request.getCalendarId();

		Calendar calendar = commonService.getCalendar(userId, calendarId);

		Schedule schedule = Schedule.of(request, calendar);
		// TODO: 장소, To-do 추가
		scheduleRepository.save(schedule);

		return ScheduleDto.Response.from(schedule);
	}

	public Pair<LocalDate, LocalDate> getStartDateAndEndDate(LocalDate referenceDate, PeriodType period) {
		return switch(period){
			case  MONTHLY ->  // 월간 조회
				Pair.of(referenceDate.withDayOfMonth(1), referenceDate.withDayOfMonth(referenceDate.lengthOfMonth()));
			case WEEKLY -> { // 주간 조회 (일요일 ~ 토요일 기준)
				LocalDate startWeek = referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
				yield Pair.of(startWeek, startWeek.plusDays(6));
			}
			case DAILY -> // 일일 조회
				Pair.of(referenceDate, referenceDate);
			default -> throw new ScheduleException(INVALID_PERIOD);
		};
	}

	public Map<?, List<Response>> getSchedulesByPeriod(
		Long userId, Long calendarId, LocalDate referenceDate, PeriodType period) {

		commonService.validateCalendarMember(userId, calendarId);

		Pair<LocalDate, LocalDate> pair = getStartDateAndEndDate(referenceDate, period);
		LocalDate startDate = pair.getLeft();
		LocalDate endDate = pair.getRight();

		List<Schedule> schedules = scheduleRepository.findByCalendarIdAndStartDateBetween(
			calendarId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

		if (period == DAILY) {
			// 시간별로 그룹화
			return schedules.stream()
				.map(ScheduleDto.Response::from)
				.collect(Collectors.groupingBy(
					schedule -> schedule.getStartDate().withSecond(0).withNano(0),
					Collectors.toList()
				));
		}

		// 날짜별로 그룹화
		return schedules.stream()
			.collect(Collectors.groupingBy(
				schedule -> schedule.getStartDate().toLocalDate(),
				Collectors.mapping(Response::from, Collectors.toList())
			));
	}

	public ScheduleDto.Response read(Long userId, Long calendarId, Long id) {
		return ScheduleDto.Response.from(commonService.getSchedule(userId, calendarId, id));
	}

	@Transactional
	public ScheduleDto.Response update(ScheduleDto.Request request) {
		Schedule schedule = commonService.getSchedule(request.getUserId(), request.getCalendarId(), request.getId());

		if (request.getTitle() != null) schedule.setTitle(request.getTitle());
		if (request.getStartDate() != null) schedule.setStartDate(request.getStartDate());
		if (request.getStartDate() != null) schedule.setEndDate(request.getEndDate());
		if (request.getMemo() != null) schedule.setMemo(request.getMemo());
		// TODO: 장소, To-do 추가

		scheduleRepository.save(schedule);

		// @Cacheable 로 캐시 저장할 때와 반환 값이 달라서 redisTemplate 사용해 캐시 업데이트
		redisTemplate.opsForValue().set("schedule::" + request.getCalendarId() + ":" + request.getId(), schedule);

		return ScheduleDto.Response.from(schedule);
	}

	@Transactional
	@CacheEvict(value = "schedule",  key = "#calendarId + ':' + #id")
	public void delete(Long userId, Long calendarId, Long id) {
		scheduleRepository.delete(commonService.getSchedule(userId, calendarId, id));
	}

}
