package com.zero.pennywise.model.entity;

import jakarta.persistence.CascadeType;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoriesEntity category;

  @Column(nullable = true)
  private Long amount;
}
