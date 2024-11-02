//package com.zero.pennywise.utils;
//
//import com.zero.pennywise.model.request.budget.BalancesDTO;
//import java.util.Collections;
//import java.util.List;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//public class PageUtils {
//
//  // PageRequest 처리 (1 페이지가 0으로 시작하는 경우)
//  public static Pageable page(Pageable page) {
//    int pageNumber = page.getPageNumber() > 0 ? page.getPageNumber() - 1 : 0;
//    return PageRequest.of(pageNumber, page.getPageSize());
//  }
//
//
//  public static Page<BalancesDTO> getPagedBalanceData(List<BalancesDTO> balances, Pageable pageable) {
//    int pageSize = pageable.getPageSize();
//    int currentPage = pageable.getPageNumber();
//    int start = currentPage * pageSize;
//
//    if (balances == null) {
//      balances = Collections.emptyList();
//    }
//
//    if (balances.size() < start) {
//      balances = Collections.emptyList();
//    } else {
//      int end = Math.min(start + pageSize, balances.size());
//      balances = balances.subList(start, end);
//    }
//
//    return new PageImpl<>(balances, pageable, balances.size());
//  }
//
//}