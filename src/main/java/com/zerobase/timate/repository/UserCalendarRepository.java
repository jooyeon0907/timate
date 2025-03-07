package com.zerobase.timate.repository;

import com.zerobase.timate.entity.UserCalendar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    List<UserCalendar> findByUserId(Long userId);
    void deleteByCalendarId(Long calendarId);

    @Query("SELECT uc FROM UserCalendar uc JOIN FETCH uc.calendar WHERE uc.user.id = :userId")
    List<UserCalendar> findUserCalendarsWithCalendars(Long userId);


}

