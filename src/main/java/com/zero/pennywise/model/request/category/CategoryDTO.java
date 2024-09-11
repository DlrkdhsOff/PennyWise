package com.zero.pennywise.model.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

  @NotBlank(message = "카테고리를 입력해주세요.")
  private String categoryName;
}
