package com.zerobase.timate.service;

import com.zerobase.timate.dto.TodoItemDto;
import com.zerobase.timate.entity.TodoItem;
import com.zerobase.timate.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TodoItemService {

	private final CommonService commonService;

	private final TodoItemRepository todoItemRepository;


	public TodoItemDto.Response updateTodoStatus(TodoItemDto.Request request) {
		TodoItem todoItem = commonService.getTotoItem(request.getUserId(), request.getCalendarId(),
			request.getScheduleId(), request.getId());
		todoItem.setCompleted(request.isCompleted());
		return TodoItemDto.Response.from(todoItemRepository.save(todoItem));
	}
}
