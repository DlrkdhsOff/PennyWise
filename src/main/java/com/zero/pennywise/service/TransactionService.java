package com.zero.pennywise.service;

import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {

  ResultResponse getTransactionInfo(HttpServletRequest request, TransactionInfoDTO transactionInfo);

  ResultResponse createTransaction(HttpServletRequest request, TransactionDTO transactionDTO);

  ResultResponse deleteTransaction(HttpServletRequest request, Long trasactionId);
}
