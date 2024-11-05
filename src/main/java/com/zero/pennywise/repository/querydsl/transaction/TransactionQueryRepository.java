package com.zero.pennywise.repository.querydsl.transaction;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  PageResponse<Transactions> getTransactionInfo(UserEntity user, TransactionInfoDTO transactionInfoDTO, int page);
}
