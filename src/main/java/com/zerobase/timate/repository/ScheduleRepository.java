package com.zerobase.timate.repository;

import com.zerobase.timate.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List<Schedule> findByCalendarIdAndStartDateBetween(Long calendarId, LocalDateTime startDate, LocalDateTime endDate);

}
