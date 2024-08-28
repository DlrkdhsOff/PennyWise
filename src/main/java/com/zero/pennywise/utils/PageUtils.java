package com.zero.pennywise.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {

  public static Pageable page(Pageable page) {

    int pageNumber = page.getPageNumber() > 0 ? page.getPageNumber() -1 : 0;
    return PageRequest.of(pageNumber, page.getPageSize());
  }
}
