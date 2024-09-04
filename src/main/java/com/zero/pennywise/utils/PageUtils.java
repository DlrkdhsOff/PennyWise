package com.zero.pennywise.utils;

import com.zero.pennywise.model.response.Categories;
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

  public static Page<Categories> getPagedData(List<Categories> categories, Pageable pageable) {
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int start = currentPage * pageSize;

    List<Categories> pagedList;

    // 데이터 범위 확인 및 페이징 처리
    if (categories.size() < start) {
      pagedList = Collections.emptyList();
    } else {
      int end = Math.min(start + pageSize, categories.size());
      pagedList = categories.subList(start, end);
    }

    return new PageImpl<>(pagedList, pageable, categories.size());
  }
}
