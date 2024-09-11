package com.zero.pennywise.model.response.transaction;

import com.zero.pennywise.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndSavindDTO {

  private Long sum;
  private UserEntity user;
  private Long categoryId;
}
