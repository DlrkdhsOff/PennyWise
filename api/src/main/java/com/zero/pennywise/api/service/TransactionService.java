package com.zero.pennywise.api.service;

import com.zero.pennywise.domain.model.request.transaction.TransactionDTO;
import com.zero.pennywise.domain.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.domain.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {

  ResultResponse getTransactionInfo(HttpServletRequest request, TransactionInfoDTO transactionInfo);

  ResultResponse createTransaction(HttpServletRequest request, TransactionDTO transactionDTO);

  ResultResponse deleteTransaction(HttpServletRequest request, Long trasactionId);
}
