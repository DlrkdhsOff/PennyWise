package com.zero.pennywise.model.request.savings;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteSavingsDTO {

  @NotBlank(message = "저축명을 입력해주세요")
  private String name;
}
