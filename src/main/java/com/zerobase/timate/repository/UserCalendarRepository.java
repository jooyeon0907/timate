package com.zerobase.timate.repository;

import com.zerobase.timate.entity.UserCalendar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {
    Optional<UserCalendar> findByUserIdAndCalendarId(Long userId, Long calendarId);
    boolean existsByUserIdAndCalendarId(Long userId, Long calendarId);
    List<UserCalendar> findByUserId(Long userId);
    List<UserCalendar> findByCalendarId(Long calendarId);
    void deleteByCalendarId(Long calendarId);
    void deleteById_UserIdAndId_CalendarId(Long userId, Long calendarId);

    @Query("SELECT uc FROM UserCalendar uc JOIN FETCH uc.calendar WHERE uc.user.id = :userId")
    List<UserCalendar> findUserCalendarsWithCalendars(Long userId);


}

