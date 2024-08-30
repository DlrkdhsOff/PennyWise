//package com.zero.pennywise.repository.querydsl;
//
//import com.zero.pennywise.model.response.TransactionsDTO;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface TransactionQueryRepository {
//
//  Page<TransactionsDTO> getAllTransaction(Long userId, Pageable page);
//
//  Page<TransactionsDTO> getTransactionsByCategory(Long userId, String categoryName, Pageable page);
//
//  void updateFixedTransaction(String lastMonthsDate, String today);
//
//}
