package com.zerobase.timate.entity;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendarId implements Serializable {

  private Long userId;
  private Long calendarId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserCalendarId that = (UserCalendarId) o;
    return Objects.equals(userId, that.userId) &&
            Objects.equals(calendarId, that.calendarId);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
