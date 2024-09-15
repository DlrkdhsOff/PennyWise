package com.zero.pennywise.model.request.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}
