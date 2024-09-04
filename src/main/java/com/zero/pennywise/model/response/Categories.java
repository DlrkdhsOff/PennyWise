package com.zero.pennywise.model.response;

import com.zero.pennywise.entity.CategoriesEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categories {

  private Long categoryId;
  private String categoryName;



}
