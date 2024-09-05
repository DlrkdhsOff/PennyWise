package com.zero.pennywise.utils;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.model.request.budget.Balances;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {

  // PageRequest 처리 (1 페이지가 0으로 시작하는 경우)
  public static Pageable page(Pageable page) {
    int pageNumber = page.getPageNumber() > 0 ? page.getPageNumber() - 1 : 0;
    return PageRequest.of(pageNumber, page.getPageSize());
  }

  // 공통 페이징 처리 메서드
  public static <T> Page<T> getPagedData(List<T> dataList, Pageable pageable) {
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int start = currentPage * pageSize;

    // 데이터 범위 확인 및 페이징 처리
    if (dataList.size() < start) {
      dataList = Collections.emptyList();
    } else {
      int end = Math.min(start + pageSize, dataList.size());
      dataList = dataList.subList(start, end);
    }

    return new PageImpl<>(dataList, pageable, dataList.size());
  }

  // 카테고리명만 출력하는 메서드
  private static List<String> getCategoryNames(List<CategoriesEntity> categories) {
    List<String> result = new ArrayList<>();
    for (CategoriesEntity category : categories) {
      result.add(category.getCategoryName());
    }
    return result;
  }

  // 카테고리 페이징 처리
  public static Page<String> getPagedCategoryData(List<CategoriesEntity> categories, Pageable pageable) {
    List<String> categoryNames = getCategoryNames(categories); // 카테고리명을 추출
    return getPagedData(categoryNames, pageable); // 공통 페이징 처리 메서드 사용
  }

  // 예산 페이징 처리
  public static Page<Balances> getPagedBalanceData(List<Balances> balances, Pageable pageable) {
    return getPagedData(balances, pageable); // 공통 페이징 처리 메서드 사용
  }

}