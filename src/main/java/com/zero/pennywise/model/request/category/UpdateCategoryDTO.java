package com.zero.pennywise.model.request.category;

import com.zero.pennywise.entity.CategoryEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UpdateCategoryDTO {

  @NotEmpty(message = "변경 전 카테고리명을 입력해주세요")
  private String beforecategoryName;

  @NotEmpty(message = "변경할 카테고리 명을 입력해주세요")
  private String afterCategoryName;

  public static CategoryEntity of(CategoryEntity category, UpdateCategoryDTO UpdateCategoryDTO) {
    return CategoryEntity.builder()
        .categoryId(category.getCategoryId())
        .categoryName(UpdateCategoryDTO.getAfterCategoryName())
        .user(category.getUser())
        .build();
  }

}
