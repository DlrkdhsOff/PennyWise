package com.zero.pennywise.model.request.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryDTO {

  @NotEmpty(message = "변경 전 카테고리명을 입력해주세요")
  private String beforecategoryName;

  @NotEmpty(message = "변경할 카테고리 명을 입력해주세요")
  private String afterCategoryName;

}
