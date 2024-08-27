package com.zero.pennywise.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  private String type;

  @Column(nullable = false)
  private Long amount;

  private String description;

  @Column(nullable = false)
  private String dateTime;

  @Column(nullable = false)
  private boolean isFixed;

}
