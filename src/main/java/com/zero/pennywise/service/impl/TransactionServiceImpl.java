package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.handler.BalanceHandler;
import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.TransactionHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.TokenType;
import com.zero.pennywise.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final JwtUtil jwtUtil;
  private final UserHandler userHandler;
  private final CategoryHandler categoryHandler;
  private final TransactionHandler transactionHandler;
  private final BalanceHandler balanceHandler;

  // 요청 헤더에서 사용자 정보를 추출하여 반환
  private UserEntity fetchUser(HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    return userHandler.findByUserId(userId);
  }

  // 사용자와 카테고리 이름으로 카테고리 엔티티 조회
  private CategoryEntity fetchCategory(UserEntity user, String categoryName) {
    return categoryHandler.findCategory(user, categoryName);
  }

  // 거래 목록 조회 기능
  @Override
  public ResultResponse getTransactionInfo(TransactionInfoDTO transactionInfoDTO, int page, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    PageResponse<Transactions> response = transactionHandler.getTransactionInfo(user, transactionInfoDTO, page);
    return new ResultResponse(SuccessResultCode.SUCCESS_GET_TRANSACTION_INFO, response);
  }

  // 거래 등록 기능
  @Override
  public ResultResponse setTransaction(TransactionDTO transactionDTO, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    CategoryEntity category = fetchCategory(user, transactionDTO.getCategoryName());

    // 거래 생성 후 잔액 업데이트
    TransactionEntity transaction = TransactionDTO.of(user, category, transactionDTO);
    balanceHandler.addBalance(user, transaction);
    transactionHandler.saveTransaction(transaction);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_TRANSACTION);
  }

  // 거래 수정 기능
  @Override
  public ResultResponse updateTransaction(UpdateTransactionDTO updateTransactionDTO, HttpServletRequest request) {
    UserEntity user = fetchUser(request);

    // 기존 거래와 수정할 카테고리 조회 후 잔액 업데이트
    TransactionEntity transaction = transactionHandler.findByTransactionId(user, updateTransactionDTO.getTransactionId());
    CategoryEntity category = fetchCategory(user, updateTransactionDTO.getCategoryName());

    TransactionEntity updatedTransaction = UpdateTransactionDTO.of(transaction, category, updateTransactionDTO);
    balanceHandler.updateBalance(user, transaction, updatedTransaction);
    transactionHandler.saveTransaction(updatedTransaction);

    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_TRANSACTION);
  }

  // 거래 삭제 기능
  @Override
  public ResultResponse deleteTransaction(Long transactionId, HttpServletRequest request) {
    UserEntity user = fetchUser(request);

    // 거래 삭제 후 잔액 조정
    TransactionEntity transaction = transactionHandler.findByTransactionId(user, transactionId);
    balanceHandler.deleteBalance(user, transaction);
    transactionHandler.deleteTransaction(transaction);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_TRANSACTION);
  }
}