package com.zero.pennywise.core.service.impl;

import com.zero.pennywise.core.component.FinanceFacade;
import com.zero.pennywise.core.component.UserFacade;
import com.zero.pennywise.domain.entity.CategoryEntity;
import com.zero.pennywise.domain.entity.TransactionEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.request.transaction.TransactionDTO;
import com.zero.pennywise.domain.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.response.page.PageResponse;
import com.zero.pennywise.domain.model.response.transaction.Transactions;
import com.zero.pennywise.domain.model.type.SuccessResultCode;
import com.zero.pennywise.core.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final UserFacade userFacade;
  private final FinanceFacade financeFacade;

  /**
   * 사용자의 거래 내역 조회.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param transactionInfo 거래 조회 조건을 담은 DTO
   * @return 거래 내역과 성공 코드를 포함한 ResultResponse
   */
  @Override
  public ResultResponse getTransactionInfo(HttpServletRequest request, TransactionInfoDTO transactionInfo) {
    UserEntity user = userFacade.getUserByAccessToken(request);
    PageResponse<Transactions> transactionsList = PageResponse
        .of(financeFacade.getTransactionList(user, transactionInfo), transactionInfo.getPage());

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_TRANSACTION_INFO, transactionsList);
  }

  /**
   * 새로운 거래 생성.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param transactionDTO 생성할 거래 정보를 담은 DTO
   * @return 거래 생성 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) {
    UserEntity user = userFacade.getUserByAccessToken(request);
    CategoryEntity category = financeFacade.findCategory(user, transactionDTO.getCategoryName());

    TransactionEntity transaction = financeFacade.createAndSaveTransaction(user, category, transactionDTO);
    financeFacade.checkBudget(transaction);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_TRANSACTION);
  }

  /**
   * 특정 거래 삭제.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param transactionId 삭제할 거래의 고유 식별자
   * @return 거래 삭제 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse deleteTransaction(HttpServletRequest request, Long transactionId) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    TransactionEntity transaction = financeFacade.findTransaction(user, transactionId);
    financeFacade.deleteTransaction(transaction);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_TRANSACTION);
  }

}