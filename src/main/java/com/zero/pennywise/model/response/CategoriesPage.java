package com.zero.pennywise.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class CategoriesPage {

  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<String> categories;

  public static CategoriesPage of(Page<String> pageList) {
    return new CategoriesPage(
        pageList.getNumber() + 1,
        pageList.getTotalPages(),
        pageList.getTotalElements(),
        pageList.getContent()
    );

  }
}
