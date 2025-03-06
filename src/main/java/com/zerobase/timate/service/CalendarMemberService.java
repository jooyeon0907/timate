package com.zerobase.timate.service;

import static com.zerobase.timate.type.ErrorCode.CALENDAR_DELETION_REQUIRED;
import static com.zerobase.timate.type.ErrorCode.EXISTS_CALENDAR_MEMBER;
import static com.zerobase.timate.type.ErrorCode.MEMBER_LIST_REQUIRED;
import static com.zerobase.timate.type.ErrorCode.SELF_PERMISSION_TRANSFER;

import com.zerobase.timate.dto.UserCalendarDto;
import com.zerobase.timate.dto.UserDto;
import com.zerobase.timate.entity.Calendar;
import com.zerobase.timate.entity.MemberRole;
import com.zerobase.timate.entity.User;
import com.zerobase.timate.entity.UserCalendar;
import com.zerobase.timate.entity.UserCalendarId;
import com.zerobase.timate.exception.CalendarException;
import com.zerobase.timate.repository.UserCalendarRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarMemberService {

	private final CommonService commonService;
	private final CalendarService calendarService;

	private final UserCalendarRepository userCalendarRepository;


	public UserCalendarDto.Response addMember(Long userId, Long calendarId) {

		log.info("캘린더 멤버 추가 요청 - userId: {}, calendarId: {}", userId, calendarId);
		User user = commonService.getUserById(userId);
		Calendar calendar = calendarService.getCalendarById(calendarId);


		// 이미 초대된 멤버인지 확인
		if (commonService.isCalendarMember(user.getId(), calendarId)){
			log.warn("이미 초대된 사용자입니다 - userId: {}, calendarId: {}", user.getId(), calendarId);
			throw new CalendarException(EXISTS_CALENDAR_MEMBER);
		}

		UserCalendarId userCalendarId = new UserCalendarId(calendarId, user.getId());
		UserCalendar userCalendar = new UserCalendar().builder()
			.id(userCalendarId)
			.user(user)
			.calendar(calendar)
			.role(MemberRole.MEMBER)
			.build();
		userCalendarRepository.save(userCalendar);
		log.info("캘린더 멤버 추가 완료! - userId: {}, calendarId: {}", user.getId(), calendar.getId());

		return UserCalendarDto.Response.from(userCalendar);

	}

	public List<UserDto.Response> memberList(Long userId, Long calendarId) {
		// 해당 캘린더의 멤버인지 확인
		commonService.checkCalendarMember(userId, calendarId);

		List<UserCalendar> userCalendars = userCalendarRepository.findByCalendarId(calendarId);

		return userCalendars.stream()
			.map(userCalendar -> UserDto.Response.from(userCalendar.getUser()))
			.collect(Collectors.toList());
	}

	@Transactional
	public void exit(Long userId, Long calendarId) {
		// 해당 사용자가 캘린더의 멤버인지 확인
		UserCalendar userCalendar = commonService.getUserCalendar(userId, calendarId);

		Calendar calendar = userCalendar.getCalendar();

		// 관리자라면
		if (userCalendar.getRole().equals(MemberRole.MASTER)){
			// 멤버 수 확인 (권한 양도를 위해서)
 			List<UserCalendar> members = userCalendarRepository.findByCalendarId(calendar.getId());

            List<UserDto.Response> memberList = members.stream()
                .filter(m -> !m.getUser().getId().equals(userId)) // 본인 제외
                .map(m -> UserDto.Response.from(m.getUser()))
                .collect(Collectors.toList());

			if (memberList.size() > 0) {
				// 멤버 목록을 반환하여 프론트에서 새로운 생성자 선택
 				throw new CalendarException(MEMBER_LIST_REQUIRED, memberList);

			} else {
				// 본인만 남았다면 삭제 요청을 프론트에서 받도록 안내
				 throw new CalendarException(CALENDAR_DELETION_REQUIRED);
			}
		}

		// 일반 멤버라면 단순 나가기
		exitCalendar(userId, calendar.getId());
	}

	@Transactional
	public void transferMasterAndExit(UserCalendarDto.Request request) {
		Long userId = request.getUserId();
		Long calendarId = request.getCalendarId();
		Long newMasterId = request.getNewMasterId();

		// 해당 사용자 권한이 MASTER 인지 확인
		commonService.checkCalendarMaster(userId, calendarId);

		changeMaster(userId, newMasterId, calendarId);

		exitCalendar(userId, calendarId);

	}

	@Transactional
	private void exitCalendar(Long userId, Long calendarId) {
		userCalendarRepository.deleteById_UserIdAndId_CalendarId(userId, calendarId);
		log.info("캘린더 퇴장 완료 - userId: {}, calendarId: {}", userId, calendarId);
	}

	@Transactional
	private void changeMaster(Long userId, Long newMasterId, Long calendarId) {
		// 권한 양도하려는 사용자가 본인이면 안되므로 확인
		if (newMasterId == userId) {
			throw new CalendarException(SELF_PERMISSION_TRANSFER);
		}


		UserCalendar userCalendar = commonService.getUserCalendar(newMasterId, calendarId);
		userCalendar.setRole(MemberRole.MASTER);
		userCalendarRepository.save(userCalendar);
	}



}
