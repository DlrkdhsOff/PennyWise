package com.zero.pennywise.model.request.transaction;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTransactionDTO {

  @NotNull(message = "거래 아이디를 입력해주세요.")
  private Long transactionId;

  @NotBlank(message = "카테고리명를 입력해주세요.")
  private String categoryName;

  @NotBlank(message = "지출/수입 또는 고정 지출/ 고정 수입을 입력해주세요.")
  private String type;

  @NotNull(message = "금액을 입력해주세요.")
  private Long amount;

  private String description;

  public static TransactionEntity of(UserEntity user, CategoryEntity category, UpdateTransactionDTO updateTransactionDTO) {
    return TransactionEntity.builder()
        .transactionId(updateTransactionDTO.getTransactionId())
        .user(user)
        .category(category)
        .type(TransactionType.getTransactionType(updateTransactionDTO.type))
        .amount(updateTransactionDTO.getAmount())
        .description(updateTransactionDTO.getDescription())
        .dateTime(LocalDateTime.now())
        .build();
  }

  public static TransactionEntity of(TransactionEntity transaction, CategoryEntity category, UpdateTransactionDTO updateTransactionDTO) {
    return TransactionEntity.builder()
        .transactionId(transaction.getTransactionId())
        .user(transaction.getUser())
        .category(category)
        .type(TransactionType.getTransactionType(updateTransactionDTO.type))
        .amount(updateTransactionDTO.getAmount())
        .description(updateTransactionDTO.getDescription())
        .dateTime(transaction.getDateTime())
        .build();
  }
}
