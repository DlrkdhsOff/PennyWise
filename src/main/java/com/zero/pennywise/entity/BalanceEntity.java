package com.zero.pennywise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Balance")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BalanceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long balanceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @Column(nullable = false)
  private Long totalIncomeAmount;

  @Column(nullable = false)
  private Long totalExpensesAmount;

  @Column(nullable = false)
  private String recordMonth;


  // 수입 금액 추가
  public void addIncomeAmount(Long amount) {
    this.totalIncomeAmount += amount;
  }

  // 지출 금액 추가
  public void addExpensesAmount(Long amount) {
    this.totalExpensesAmount += amount;
  }

  // 수입 금액 차감
  public void subtractIncomeAmount(Long amount) {
    this.totalIncomeAmount -= amount;
  }

  // 지출 금액 차감
  public void subtractExpensesAmount(Long amount) {
    this.totalExpensesAmount -= amount;
  }
}
