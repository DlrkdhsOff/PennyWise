package com.zero.pennywise.controller;

import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

  private final TransactionService transactionService;

  // 거래 목록 출력
  @GetMapping
  public ResponseEntity<ResultResponse> getTransactionList(
      @RequestParam(value = "transactionType", required = false) String transactionType,
      @RequestParam(value = "categoryName", required = false) String categoryName,
      @RequestParam(value = "month", required = false) Long month,
      @RequestParam("page") int page,
      HttpServletRequest request)
  {
    TransactionInfoDTO transactionInfoDTO = new TransactionInfoDTO(
        transactionType, categoryName, month);

    ResultResponse resultResponse = transactionService.getTransactionInfo(transactionInfoDTO, page, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 거래 등록
  @PostMapping
  public ResponseEntity<ResultResponse> transaction(
      @RequestBody @Valid TransactionDTO transactionDTO,
      HttpServletRequest request)
  {

    ResultResponse resultResponse = transactionService.setTransaction(transactionDTO, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 거래 수정
  @PutMapping
  public ResponseEntity<ResultResponse> updateTransaction(
      @RequestBody @Valid UpdateTransactionDTO updateTransactionDTO,
      HttpServletRequest request)
  {

    ResultResponse resultResponse = transactionService.updateTransaction(updateTransactionDTO, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 거래 삭제
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteTransaction(
      @RequestParam(name = "trasactionId") Long trasactionId,
      HttpServletRequest request)
  {

    ResultResponse resultResponse = transactionService.deleteTransaction(trasactionId, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}