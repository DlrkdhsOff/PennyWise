package com.zero.pennywise.entity;

import com.zero.pennywise.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

@Entity(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long transactionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  private long amount;

  private String description;

  LocalDateTime dateTime;
}
