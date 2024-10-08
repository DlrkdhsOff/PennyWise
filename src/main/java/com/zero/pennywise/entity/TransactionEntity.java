package com.zero.pennywise.entity;

import com.zero.pennywise.enums.TransactionStatus;
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
import lombok.Setter;

@Entity(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long transactionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(nullable = false)
  private Long categoryId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionStatus type;

  @Column(nullable = false)
  private Long amount;

  private String description;

  @Column(nullable = false, updatable = false)
  private LocalDateTime dateTime;
}
