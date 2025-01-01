package com.zero.pennywise.component.facade;

import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacadeManager {

  private final TransactionRepository transactionRepository;
  private final TransactionQueryRepository transactionQueryRepository;

}
