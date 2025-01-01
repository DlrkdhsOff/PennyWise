package com.zero.pennywise.service;

import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {

  ResultResponse getTransactionInfo(TransactionInfoDTO transactionInfoDTO, int page, HttpServletRequest request);

  ResultResponse setTransaction(TransactionDTO transactionDTO, HttpServletRequest request);

  ResultResponse deleteTransaction(Long trasactionId, HttpServletRequest request);
}
