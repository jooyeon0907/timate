package com.zerobase.timate.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long creatorId;

  @Column(length = 50, nullable = false)
  private String title;
  @Column(nullable = false)
  private LocalDateTime startDate;
  @Column(nullable = false)
  private LocalDateTime endDate;

  @Column(columnDefinition = "TEXT")
  private String memo;
  private Long googleEventId;

  // 장소 관련
  @Column(length = 100)
  private String placeName;
  @Column(length = 200)
  private String address;
  private double latitude;
  private double longitude;


  @ManyToOne
  @JoinColumn(name = "calendar_id")
  private Calendar calendar;


}
