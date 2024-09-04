package com.zero.pennywise.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBalance {

  private String categoryName;
  private Long balance;

}
