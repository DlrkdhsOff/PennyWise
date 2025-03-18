//package com.zero.pennywise.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import com.zero.pennywise.component.facade.UserFacade;
//import com.zero.pennywise.entity.CategoryEntity;
//import com.zero.pennywise.entity.TransactionEntity;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.model.type.TransactionType;
//import com.zero.pennywise.exception.GlobalException;
//import com.zero.pennywise.model.request.transaction.TransactionDTO;
//import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.model.response.page.PageResponse;
//import com.zero.pennywise.model.response.transaction.Transactions;
//import com.zero.pennywise.model.type.FailedResultCode;
//import com.zero.pennywise.model.type.SuccessResultCode;
//import com.zero.pennywise.model.type.UserRole;
//import jakarta.servlet.http.HttpServletRequest;
//import java.time.LocalDateTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//class TransactionServiceImplTest {
//
//  @Mock
//  private UserFacade userFacade;
//
//  @Mock
//  private HttpServletRequest request;
//
//  @InjectMocks
//  private TransactionServiceImpl transactionService;
//
//  private final int PAGE = 1;
//  private final Long TRANSACTION_ID = 1L;
//
//  private PageResponse<Transactions> pageResponse;
//  private TransactionInfoDTO transactionInfoDTO;
//  private TransactionDTO transactionDTO;
//  private TransactionEntity transaction;
//
//  @BeforeEach
//  void setUp() {
//    List<Transactions> transactionsList = List.of(
//        new Transactions(1L, "지출", "카테고리1", 1000L, "상세 설명 1", "시간1"),
//        new Transactions(2L, "지출", "카테고리1", 2000L, "상세 설명 2", "시간2")
//    );
//
//    pageResponse = PageResponse.of(transactionsList, PAGE);
//
//    transactionInfoDTO = new TransactionInfoDTO("지출", "카테고리1", 3L);
//
//    transactionDTO = new TransactionDTO("카테고리1", "지출", 1000L, "상세 설명");
//
//    UserEntity user = UserEntity.builder()
//        .userId(1L)
//        .email("email")
//        .password("password")
//        .nickname("nickname")
//        .role(UserRole.USER)
//        .build();
//
//
//    CategoryEntity category = CategoryEntity.builder()
//        .categoryId(1L)
//        .categoryName("카테고리1")
//        .user(user)
//        .build();
//
//
//    transaction = TransactionEntity.builder()
//        .transactionId(1L)
//        .user(user)
//        .category(category)
//        .type(TransactionType.getTransactionType("지출"))
//        .amount(1000L)
//        .description("거래 상세정보")
//        .dateTime(LocalDateTime.now())
//        .build();
//  }
//
//  @Test
//  @DisplayName("거래 목록 조회 : 성공")
//  void getTransactionInfo() {
//    // given
//    when(userFacade.getTransactionList(request, transactionInfoDTO, PAGE))
//        .thenReturn(pageResponse);
//
//    // when
//    ResultResponse response = transactionService
//        .getTransactionInfo(transactionInfoDTO, PAGE, request);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_GET_TRANSACTION_INFO.getStatus(), response.getStatus());
//  }
//
//  @Test
//  @DisplayName("거래 목록 조회 : 실패 - 존재하지 않은 사용자")
//  void getTransactionInfo_Failed_UserNotFound() {
//    when(userFacade.getTransactionList(request, transactionInfoDTO, PAGE)).thenThrow(new GlobalException(
//        FailedResultCode.USER_NOT_FOUND));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> transactionService.getTransactionInfo(transactionInfoDTO, PAGE, request));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("거래 등록 : 성공")
//  void setTransaction() {
//    // given
//    when(userFacade.createTransaction(request, transactionDTO))
//        .thenReturn(transaction);
//
//    // when
//    ResultResponse response = transactionService
//        .setTransaction(transactionDTO, request);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_CREATE_TRANSACTION.getStatus(), response.getStatus());
//  }
//
//  @Test
//  @DisplayName("거래 등록 : 실패 - 존재하지 않은 사용자")
//  void setTransaction_Failed_UserNotFound() {
//    when(userFacade.createTransaction(request, transactionDTO)).thenThrow(new GlobalException(
//        FailedResultCode.USER_NOT_FOUND));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> transactionService.setTransaction(transactionDTO, request));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("거래 등록 : 실패 - 존재하지 않은 카테고리")
//  void setTransaction_Failed_CategoryNotFound() {
//    when(userFacade.createTransaction(request, transactionDTO)).thenThrow(new GlobalException(
//        FailedResultCode.CATEGORY_NOT_FOUND));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> transactionService.setTransaction(transactionDTO, request));
//
//    // then
//    assertEquals(
//        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("거래 삭제 : 성공")
//  void deleteTransaction() {
//    // given & when
//    ResultResponse response = transactionService.deleteTransaction(request, TRANSACTION_ID);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_DELETE_CATEGORY.getStatus(), response.getStatus());
//  }
//
//  @Test
//  @DisplayName("거래 삭제 : 실패 - 존재하지 않은 사용자")
//  void deleteTransaction_Failed_UserNotFound() {
//    // given
//    doThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND))
//        .when(userFacade).deleteTransaction(request, TRANSACTION_ID);
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> transactionService.deleteTransaction(request, TRANSACTION_ID));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("거래 삭제 : 실패 - 존재하지 않은 거래 내역")
//  void deleteTransaction_Failed_TransactionNotFound() {
//    // given
//    doThrow(new GlobalException(FailedResultCode.TRANSACTION_NOT_FOUND))
//        .when(userFacade).deleteTransaction(request, TRANSACTION_ID);
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> transactionService.deleteTransaction(request, TRANSACTION_ID));
//
//    // then
//    assertEquals(
//        FailedResultCode.TRANSACTION_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//}