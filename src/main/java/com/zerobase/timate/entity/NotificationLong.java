package com.zerobase.timate.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class NotificationLong {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String content;

  @CreatedDate
  private LocalDateTime updateAt;

  @PrePersist
  public void prePersist() {
    if (updateAt == null) {
      updateAt = LocalDateTime.now();
    }
  }

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(50)", nullable = false)
  private LogType type;
  private Long relatedId;

}
