package com.zero.pennywise.model.request.category;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryDTO {

  @NotBlank(message = "변경 전 카테고리명을 입력해주세요")
  private String categoryName;

  @NotBlank(message = "변경할 카테고리 명을 입력해주세요")
  private String newCategoryName;

}
