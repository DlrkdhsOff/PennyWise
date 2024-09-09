package com.zero.pennywise.model.response.category;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
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
