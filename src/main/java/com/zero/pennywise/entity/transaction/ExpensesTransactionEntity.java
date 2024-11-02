package com.zero.pennywise.entity.transaction;

import com.zero.pennywise.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "expenses_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ExpensesTransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne (fetch = FetchType.LAZY)
  private TransactionEntity transaction;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  private long amount;

  private String description;

  @Column(nullable = false, updatable = false)
  private LocalDateTime dateTime;
}
