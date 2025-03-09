package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.INVALID_PERIOD;
import static com.zerobase.timate.type.ErrorCode.TODO_NOT_FOUND;

import com.zerobase.timate.dto.ScheduleDto;
import com.zerobase.timate.dto.ScheduleDto.Response;
import com.zerobase.timate.dto.TodoItemDto;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.Schedule;
import com.zerobase.timate.entity.TodoItem;
import com.zerobase.timate.exception.ScheduleException;
import com.zerobase.timate.repository.ScheduleRepository;
import com.zerobase.timate.repository.TodoItemRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final CommonService commonService;

	private final ScheduleRepository scheduleRepository;
	private final TodoItemRepository todoItemRepository;


	@Transactional
	public ScheduleDto.Response create(ScheduleDto.Request request) {
		Long userId = request.getUserId();
		Long calendarId = request.getCalendarId();

		Calendar calendar = commonService.getCalendar(userId, calendarId);

		Schedule schedule = Schedule.builder()
			.creatorId(userId)
			.calendar(calendar)
			.title(request.getTitle())
			.startDate(request.getStartDate())
			.endDate(request.getEndDate())
			.memo(request.getMemo())
			.placeName(request.getPlaceName())
			.address(request.getAddress())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.build();
		scheduleRepository.save(schedule);

		// 투두리스트 항목 저장
		saveTodoItems(schedule, request.getTodoItems());

		return ScheduleDto.Response.from(schedule);
	}

	public Map<?, List<Response>> getSchedulesByPeriod(
		Long userId, Long calendarId, LocalDate referenceDate, String period) {

		commonService.validateCalendarMember(userId, calendarId);

		LocalDate startDate;
		LocalDate endDate;

		switch (period.toLowerCase()) {
			case "monthly": // 월간 조회
				startDate = referenceDate.withDayOfMonth(1);
				endDate = referenceDate.withDayOfMonth(referenceDate.lengthOfMonth());
				break;
			case "weekly": // 주간 조회 (일요일 ~ 토요일 기준)
				startDate = referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
				endDate = startDate.plusDays(6);
				break;
			case "daily": // 일일 조회
				startDate = referenceDate;
				endDate = referenceDate;
				break;
			default:
				throw new ScheduleException(INVALID_PERIOD);
		}

		List<Schedule> schedules = scheduleRepository.findByCalendarIdAndStartDateBetween(
			calendarId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
		if (period.equals("daily")) {
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
		return ScheduleDto.Response.withTodoFrom(commonService.getSchedule(userId, calendarId, id));
	}

	@Transactional
	public ScheduleDto.Response update(ScheduleDto.Request request) {
		Schedule schedule = commonService.getSchedule(request.getUserId(), request.getCalendarId(), request.getId());

		if (request.getTitle() != null) schedule.setTitle(request.getTitle());
		if (request.getStartDate() != null) schedule.setStartDate(request.getStartDate());
		if (request.getStartDate() != null) schedule.setEndDate(request.getEndDate());
		if (request.getMemo() != null) schedule.setMemo(request.getMemo());
		if (request.getPlaceName() != null) schedule.setPlaceName(request.getPlaceName());
		if (request.getAddress() != null) schedule.setAddress(request.getAddress());
		if (request.getLatitude() != 0.0) schedule.setLatitude(request.getLatitude());
		if (request.getLongitude() != 0.0) schedule.setLongitude(request.getLongitude());

		if (request.getTodoItems().size() > 0) {
			updateTodoItems(request.getTodoItems(), schedule);
		}

		scheduleRepository.save(schedule);

		return ScheduleDto.Response.withTodoFrom(schedule);
	}

	@Transactional
	public void delete(Long userId, Long calendarId, Long id) {
		scheduleRepository.delete(commonService.getSchedule(userId, calendarId, id));
	}

	private void updateTodoItems(List<TodoItemDto.Request> updatedTodoItems, Schedule schedule) {
		// 기존 투두 리스트
		List<TodoItem> existingTodoItems = todoItemRepository.findByScheduleId(schedule.getId());

		// 삭제된 항목 찾기 (기존 목록에 있었는데 새로운 목록에는 없는 것)
		List<TodoItem> toDelete = existingTodoItems.stream()
			.filter(todo -> updatedTodoItems.stream()
					.noneMatch(updatedTodo -> updatedTodo.getId().equals(todo.getId()))
			)
			.toList();

		// 삭제된 항목 제거
		if (!toDelete.isEmpty()) {
			todoItemRepository.deleteAllInBatch(toDelete);
		}

		// 기존 to do 목록 수정 또는 추가
		List<TodoItem> toSave = updatedTodoItems.stream().map(updatedTodo -> {
			if (updatedTodo.getId() == null) {
				// 새로운 항목 추가
				return new TodoItem(updatedTodo.getTask(), schedule);
			}
			// 기존 항목 수정
			return existingTodoItems.stream()
				.filter(todo -> todo.getId().equals(updatedTodo.getId()))
				.findFirst()
				.map(existingTodo -> {
					existingTodo.setTask(updatedTodo.getTask());
					return existingTodo;
				})
				.orElseThrow(() -> new ScheduleException(TODO_NOT_FOUND));
		}).toList();

		if (!toSave.isEmpty()) {
			todoItemRepository.saveAll(toSave);
		}

	}

	private void saveTodoItems(Schedule schedule, List<TodoItemDto.Request> todoItemDtoList){
		if (todoItemDtoList == null) return;

		List<TodoItem> todoItems = todoItemDtoList.stream()
			.map(todo -> new TodoItem(todo.getTask(), schedule))
			.toList();

		todoItemRepository.saveAll(todoItems);
	}

}
