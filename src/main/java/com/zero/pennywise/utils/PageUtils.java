package com.zero.pennywise.utils;

import com.zero.pennywise.entity.CategoriesEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {

  public static Pageable page(Pageable page) {

    int pageNumber = page.getPageNumber() > 0 ? page.getPageNumber() -1 : 0;
    return PageRequest.of(pageNumber, page.getPageSize());
  }

  // 캐시 데이터 페이징 처리
  public static Page<String> getPagedData(List<CategoriesEntity> categories, Pageable pageable) {
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int start = currentPage * pageSize;

    List<String> categoriesList = of(categories);

    // 데이터 범위 확인 및 페이징 처리
    if (categories.size() < start) {
      categoriesList = Collections.emptyList();
    } else {
      int end = Math.min(start + pageSize, categories.size());
      categoriesList = categoriesList.subList(start, end);
    }

    return new PageImpl<>(categoriesList, pageable, categories.size());
  }


  // 카테고리명만 출력
  private static List<String> of(List<CategoriesEntity> categories) {
    List<String> result = new ArrayList<>();
    for (CategoriesEntity category : categories) {
      result.add(category.getCategoryName());
    }

    return result;
  }
}
