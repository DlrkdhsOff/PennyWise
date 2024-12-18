package com.zero.pennywise.entity;

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

@Entity(name = "budgets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BudgetEntity extends DateEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long budgetId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  private Long amount;
}
