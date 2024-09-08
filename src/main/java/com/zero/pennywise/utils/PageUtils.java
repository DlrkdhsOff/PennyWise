package com.zero.pennywise.utils;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.model.request.budget.BalancesDTO;
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


  // 예산 페이징 처리
  public static Page<BalancesDTO> getPagedBalanceData(List<BalancesDTO> balances, Pageable pageable) {
    return getPagedData(balances, pageable); // 공통 페이징 처리 메서드 사용
  }

}