package com.zerobase.timate.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String email;
  @Column(length = 255, nullable = false)
  private String password;
  @Column(length = 50, nullable = false)
  private String name;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime registerAt;

  @PrePersist
  public void prePersist() {
    if (registerAt == null) {
      registerAt = LocalDateTime.now();
    }
  }


  @OneToMany(mappedBy = "user")
  private List<UserCalendar> userCalendars;

}
