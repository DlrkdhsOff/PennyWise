package com.zero.pennywise.service.impl;

import com.zero.pennywise.component.facade.FacadeManager;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

  @Mock
  private FacadeManager facadeManager;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private TransactionServiceImpl transactionService;

  private final int PAGE = 1;

  private PageResponse<Transactions> pageResponse;

  @BeforeEach
  void setUp() {
    List<Transactions> transactionsList = List.of(
        new Transactions(1L, "지출", "카테고리1", 1000L, "상세 설명 1", "시간1"),
        new Transactions(2L, "지출", "카테고리1", 2000L, "상세 설명 2", "시간2")
    );
  }

}