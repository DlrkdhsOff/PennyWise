package com.zero.pennywise.model.request.transaction;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

  public static TransactionEntity of(UserEntity user, CategoryEntity category, TransactionDTO transactionDTO) {
    return TransactionEntity.builder()
        .user(user)
        .category(category)
        .type(TransactionType.getTransactionType(transactionDTO.type))
        .amount(transactionDTO.getAmount())
        .description(transactionDTO.getDescription())
        .dateTime(LocalDateTime.now())
        .build();
  }
}
