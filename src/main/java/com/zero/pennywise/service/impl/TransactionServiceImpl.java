//package com.zero.pennywise.service.impl;
//
//import com.zero.pennywise.component.facade.UserFacade;
//import com.zero.pennywise.entity.TransactionEntity;
//import com.zero.pennywise.model.request.transaction.TransactionDTO;
//import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.model.response.page.PageResponse;
//import com.zero.pennywise.model.response.transaction.Transactions;
//import com.zero.pennywise.model.type.SuccessResultCode;
//import com.zero.pennywise.service.TransactionService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TransactionServiceImpl implements TransactionService {
//
//  private final UserFacade userFacade;
//
//
//
//  // 거래 목록 조회 기능
//  @Override
//  public ResultResponse getTransactionInfo(TransactionInfoDTO transactionInfoDTO, int page, HttpServletRequest request) {
//    PageResponse<Transactions> response = userFacade.getTransactionList(request, transactionInfoDTO, page);
//
//    return new ResultResponse(SuccessResultCode.SUCCESS_GET_TRANSACTION_INFO, response);
//  }
//
//  // 거래 등록 기능
//  @Override
//  public ResultResponse setTransaction(TransactionDTO transactionDTO, HttpServletRequest request) {
//    TransactionEntity transaction = userFacade.createTransaction(request, transactionDTO);
//
//    userFacade.saveTransaction(transaction);
//    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_TRANSACTION);
//  }
//
//
//  // 거래 삭제 기능
//  @Override
//  public ResultResponse deleteTransaction(HttpServletRequest request, Long transactionId) {
//    userFacade.deleteTransaction(request, transactionId);
//
//    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_TRANSACTION);
//  }
//}