package com.zerobase.timate.test;

import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.CalendarType;
import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.repository.CalendarRepository;
import com.zerobase.timate.repository.UserCalendarRepository;
import com.zerobase.timate.service.CommonService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataInsertService {

	private final CommonService commonService;
	private final CalendarRepository calendarRepository;
	private final UserCalendarRepository userCalendarRepository;
    private static final int BATCH_SIZE = 100; // 배치 크기 설정

    @Transactional
    public void insertCalendars(int count, Long userId) {
        List<Calendar> calendars = new ArrayList<>(BATCH_SIZE);
        List<UserCalendar> userCalendars = new ArrayList<>(BATCH_SIZE);
		User user = commonService.getUserById(userId);

        for (int i = 0; i < count; i++) {
			Calendar calendar = Calendar.builder()
				.name("테스트 캘린더" + i)
				.type(CalendarType.SHARED)
				.build();
            calendars.add(calendar);

			UserCalendar userCalendar = UserCalendar.builder()
				.user(user)
				.calendar(calendar)
				.role(MemberRole.MASTER)
				.build();
			userCalendars.add(userCalendar);


            // 일정 개수마다 저장 후 clear (Batch Insert 유도)
            if (calendars.size() >= BATCH_SIZE) {
                // Calendar 저장
                calendarRepository.saveAll(calendars);
                calendarRepository.flush();  // 강제 flush
                calendars.clear();  // 캐시 초기화

                // UserCalendar 저장
                userCalendarRepository.saveAll(userCalendars);
                userCalendarRepository.flush();  // 강제 flush
                userCalendars.clear();  // 캐시 초기화
            }
        }

        // 남아 있는 데이터 저장
        if (!calendars.isEmpty()) {
            calendarRepository.saveAll(calendars);
            calendarRepository.flush();

            userCalendarRepository.saveAll(userCalendars);
            userCalendarRepository.flush();
        }
    }

}
