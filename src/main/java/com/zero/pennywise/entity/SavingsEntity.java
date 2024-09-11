package com.zero.pennywise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "savings")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SavingsEntity extends DateEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long Id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  private String name;

  private Long amount;

  private String description;

  private LocalDate startDate;

  private LocalDate endDate;
}