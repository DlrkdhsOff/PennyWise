package com.zero.pennywise.model.entity;

import com.zero.pennywise.status.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long categoryId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionStatus type;

  @Column(nullable = false)
  private Long amount;

  private String description;

  @Column(nullable = false)
  private String dateTime;

  @Column(nullable = false)
  private boolean isFixed;

}
