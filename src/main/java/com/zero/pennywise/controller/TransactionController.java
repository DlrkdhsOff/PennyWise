package com.zero.pennywise.controller;

import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

  private final TransactionService transactionService;

  /**
   * 사용자의 거래 내역 조회.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param transactionInfo 거래 조회 조건을 담은 DTO (유효성 검증 포함)
   * @return 거래 내역과 HTTP 상태를 포함한 ResponseEntity
   */
  @PostMapping
  public ResponseEntity<ResultResponse> getTransactionList(HttpServletRequest request,
      @RequestBody @Valid TransactionInfoDTO transactionInfo) {
    // 거래 내역 조회 서비스 호출
    ResultResponse resultResponse = transactionService.getTransactionInfo(request, transactionInfo);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 새로운 거래 등록.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param transactionDTO 생성할 거래 정보를 담은 DTO (유효성 검증 포함)
   * @return 거래 생성 결과와 HTTP 상태를 포함한 ResponseEntity
   */
  @PostMapping("/create")
  public ResponseEntity<ResultResponse> transaction(HttpServletRequest request,
      @RequestBody @Valid TransactionDTO transactionDTO) {
    // 거래 생성 서비스 호출
    ResultResponse resultResponse = transactionService.createTransaction(request, transactionDTO);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 특정 거래 삭제.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param transactionId 삭제할 거래의 고유 식별자
   * @return 거래 삭제 결과와 HTTP 상태를 포함한 ResponseEntity
   */
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteTransaction(HttpServletRequest request,
      @RequestParam("transactionId") Long transactionId) {
    // 거래 삭제 서비스 호출
    ResultResponse resultResponse = transactionService.deleteTransaction(request, transactionId);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}