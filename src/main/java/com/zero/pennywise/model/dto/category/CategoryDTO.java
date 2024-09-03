package com.zero.pennywise.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

  @NotBlank(message = "카테고리를 입력해주세요.")
  private String categoryName;
}
