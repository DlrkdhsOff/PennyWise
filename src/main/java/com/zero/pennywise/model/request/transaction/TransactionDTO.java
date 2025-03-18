package com.zero.pennywise.model.request.transaction;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.transaction.TotalAmount;
import com.zero.pennywise.model.type.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionDTO {

  @NotBlank(message = "카테고리명을 입력해주세요.")
  private String categoryName;

  @NotBlank(message = "지출/수입을 입력해주세요.")
  private String type;

  @NotNull(message = "금액을 입력해주세요.")
  private Long amount;

  private String description;

  public static TransactionEntity of(UserEntity user, CategoryEntity category,
      TransactionDTO transactionDTO, TotalAmount totalAmount) {

    TransactionType type = TransactionType.getTransactionType(transactionDTO.getType());
    Long amount = transactionDTO.getAmount();

    Long totalIncomeAmount = type.isExpenses()
        ? totalAmount.getTotalIncomeAmount()
        : totalAmount.getTotalIncomeAmount() + amount;

    Long totalExpensesAmount = type.isExpenses()
        ? totalAmount.getTotalExpensesAmount() + amount
        : totalAmount.getTotalExpensesAmount();

    return TransactionEntity.builder()
        .user(user)
        .category(category)
        .type(type)
        .amount(amount)
        .description(transactionDTO.getDescription())
        .totalIncomeAmount(totalIncomeAmount)
        .totalExpensesAmount(totalExpensesAmount)
        .build();
  }
}