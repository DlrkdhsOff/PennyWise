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

@Entity(name = "budgets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BudgetEntity extends DateEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long budgetId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long categoryId;

  @Column(nullable = true)
  private Long amount;
}
