package com.zero.pennywise.component.handler;


import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

  private final TransactionRepository transactionRepository;
  private final TransactionQueryRepository transactionQueryRepository;

  // 거래 저장
  public void saveTransaction(TransactionEntity transaction) {
    transactionRepository.save(transaction);
  }

  // 거래 목록 조회
  public PageResponse<Transactions> getTransactionInfo(UserEntity user, TransactionInfoDTO transactionInfoDTO, int page) {
    return transactionQueryRepository.getTransactionInfo(user, transactionInfoDTO, page);

  }

  // trasactionId 값과 일치하는 객체 반환
  public TransactionEntity findByTransactionId(UserEntity user, Long trasactionId) {
    return transactionRepository.findByUserAndTransactionId(user, trasactionId)
        .orElseThrow(() -> new GlobalException(FailedResultCode.TRANSACTION_NOT_FOUND));
  }

  // 거래 삭제
  public void deleteTransaction(TransactionEntity transaction) {
    transactionRepository.delete(transaction);
  }
}