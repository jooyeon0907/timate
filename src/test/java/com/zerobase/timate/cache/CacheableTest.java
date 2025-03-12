package com.zerobase.timate.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zerobase.timate.dto.CalendarDto;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.service.CalendarService;
import com.zerobase.timate.service.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;


@SpringBootTest
class CacheableTest {

	@Autowired
    private CommonService commonService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CacheManager cacheManager;

    private Long userId;
    private Long calendarId;

    @BeforeEach
    void setUp() {
        userId = 18L;
        calendarId = 1031L;
    }

    @Test
    public void testCalendarCaching_read() {

        // 첫번째 호출 - db 에서 호출, 캐시 저장
        Calendar firstCall = commonService.getCalendar(userId, calendarId);
        assertNotNull(firstCall);

        // 두번째 호출 - 캐시 값 호출
        Calendar secondCall = commonService.getCalendar(userId, calendarId);
        assertNotNull(secondCall);

        // 첫 번째와 두 번째 호출 결과가 동일해야 함
        assertEquals(firstCall.getId(), secondCall.getId());
        assertEquals(firstCall.getName(), secondCall.getName());

        // CacheManager를 이용해 직접 캐시를 확인할 수 있음
        Cache cache = cacheManager.getCache("calendar");
        assertNotNull(cache);

        Calendar cachedValue = cache.get(userId + ":" + calendarId, Calendar.class);
        assertEquals(firstCall.getId(), cachedValue.getId());
        assertEquals(firstCall.getName(), cachedValue.getName());
    }

	@Test
    void testCacheHitPerformance() {

        // 캐시 미스 테스트 (처음 호출)
        long startTime = System.currentTimeMillis();
        commonService.getUserCalendar(userId, calendarId); // DB에서 데이터를 조회해야 함
        long firstCallDuration = System.currentTimeMillis() - startTime;

        // 캐시 히트 테스트 (두 번째 호출)
        startTime = System.currentTimeMillis();
        commonService.getUserCalendar(userId, calendarId); // 캐시에서 데이터를 조회해야 함
        long secondCallDuration = System.currentTimeMillis() - startTime;

        // 첫 번째 호출보다 두 번째 호출이 빠르게 끝나야 함 (캐시 히트)
        assertTrue(secondCallDuration < firstCallDuration, "The second call should be faster due to cache hit.");
    }

    @Test
    public void testCalendarCaching_update() {

        // 첫번째 호출 - db 에서 호출, 캐시 저장
        Calendar firstCall = commonService.getCalendar(userId, calendarId);
        assertNotNull(firstCall);

        CalendarDto.Request request = new CalendarDto.Request();
        request.setUserId(userId);
        request.setId(calendarId);
        request.setName(firstCall.getName() + 1);
        // 캐시에도 수정된 값이 업데이트 되어야 함
        calendarService.update(request);

        // 두번째 호출 - 캐시 값 호출
        Calendar secondCall = commonService.getCalendar(userId, calendarId);
        assertNotNull(secondCall);

        assertNotEquals(firstCall.getName(), secondCall.getName());

        // CacheManager를 이용해 직접 캐시를 확인할 수 있음
        Cache cache = cacheManager.getCache("calendar");
        assertNotNull(cache);

        Calendar cachedValue = cache.get(userId + ":" + calendarId, Calendar.class);
        assertNotEquals(firstCall.getName(), cachedValue.getName());
        assertEquals(secondCall.getId(), cachedValue.getId());
    }


}