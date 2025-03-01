package com.zerobase.timate.repository;

import com.zerobase.timate.entity.UserCalendar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    List<UserCalendar> findByUserId(Long userId);
    void deleteByCalendarId(Long calendarId);


}

