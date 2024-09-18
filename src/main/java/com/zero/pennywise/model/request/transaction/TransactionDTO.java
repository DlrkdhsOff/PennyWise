package com.zero.pennywise.model.request.transaction;

import static com.zero.pennywise.enums.TransactionStatus.getEnumType;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

  @NotBlank(message = "카테고리명을 입력해주세요.")
  private String categoryName;

  @NotBlank(message = "지출/수입을 입력해주세요.")
  private String type;

  @NotNull(message = "금액을 입력해주세요.")
  private Long amount;

  private String description;

  public static TransactionEntity of(UserEntity user, Long categoryId, TransactionDTO transactionDTO) {
    return TransactionEntity.builder()
        .user(user)
        .categoryId(categoryId)
        .type(getEnumType(transactionDTO.getType()))
        .amount(transactionDTO.getAmount())
        .description(transactionDTO.getDescription())
        .dateTime(LocalDateTime.now())
        .build();
  }


}
