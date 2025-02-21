package com.zerobase.timate.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendar {

  @EmbeddedId
  private UserCalendarId id = new UserCalendarId(); // 복합키 필드

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("calendarId")
  @JoinColumn(name = "calendar_id")
  private Calendar calendar;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(10) DEFAULT 'MEMBER'", nullable = false)
  private MemberRole role; // 생성자, 일반멤버


}
