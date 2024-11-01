package com.zero.pennywise.service;

import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {

  ResultResponse getAllTransactionList(int page, HttpServletRequest request);

  ResultResponse getTransactionList(String categoryName, int page, HttpServletRequest request);
}
