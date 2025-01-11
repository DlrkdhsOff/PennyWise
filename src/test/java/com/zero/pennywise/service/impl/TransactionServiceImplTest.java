package com.zero.pennywise.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.sun.net.httpserver.Authenticator.Success;
import com.zero.pennywise.component.facade.FacadeManager;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import com.zero.pennywise.model.type.SuccessResultCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
  private TransactionInfoDTO transactionInfoDTO;

  @BeforeEach
  void setUp() {
    List<Transactions> transactionsList = List.of(
        new Transactions(1L, "지출", "카테고리1", 1000L, "상세 설명 1", "시간1"),
        new Transactions(2L, "지출", "카테고리1", 2000L, "상세 설명 2", "시간2")
    );

    pageResponse = PageResponse.of(transactionsList, PAGE);

    transactionInfoDTO = new TransactionInfoDTO("지출", "카테고리1", 3L);
  }

  @Test
  @DisplayName("거래 목록 조회 : 성공")
  void getTransactionInfo() {
    // given
    when(facadeManager.getTransactionList(request, transactionInfoDTO, PAGE))
        .thenReturn(pageResponse);

    // when
    ResultResponse response = transactionService
        .getTransactionInfo(transactionInfoDTO, PAGE, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_GET_TRANSACTION_INFO.getStatus(), response.getStatus());
  }
}