package com.zero.pennywise.controller;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.service.TransactionService;
import com.zero.pennywise.utils.UserAuthorizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {

  private final TransactionService transactionService;

  // 수입 / 지출 등록
  @PostMapping("/transaction")
  public ResponseEntity<String> transaction(
      @RequestBody @Valid TransactionDTO transactionDTO)
  {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(transactionService.transaction(userId, transactionDTO));
  }

  // 거래 목록 출력 (전체 / 카테고리별)
  @GetMapping("/transaction")
  public ResponseEntity<TransactionPage> getTransactionList(
      @RequestParam(name = "categoryName", required = false) String categoryName,
      @PageableDefault(page = 1, size = 11) Pageable page)
  {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok()
        .body(transactionService.getTransactionList(userId, categoryName, page(page)));
  }

  // 거래 수정
  @PatchMapping("/transaction")
  public ResponseEntity<String> updateTransaction(
      @RequestBody @Valid UpdateTransactionDTO updateTransactionDTO)
  {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.OK)
        .body(transactionService.updateTransaction(userId, updateTransactionDTO));
  }

  // 거래 삭제
  @DeleteMapping("/transaction")
  public ResponseEntity<String> deleteTransaction(
      @RequestParam(name = "trasactionId", required = false) Long trasactionId)
  {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok()
        .body(transactionService.deleteTransaction(userId, trasactionId));
  }
}