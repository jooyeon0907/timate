package com.zerobase.timate.repository;

import com.zerobase.timate.entity.TodoItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
	boolean existsByIdAndScheduleId(Long id, Long scheduleId);
	Optional<TodoItem> findByIdAndScheduleId(Long id, Long scheduleId);


}
