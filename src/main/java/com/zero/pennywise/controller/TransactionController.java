package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.dto.UpdateTransactionDTO;
import com.zero.pennywise.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  // 수입 / 지출 등록
  @PostMapping("/transaction")
  public ResponseEntity<?> transaction(@RequestBody @Valid TransactionDTO transactionDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }


    return ResponseEntity.status(HttpStatus.CREATED)
        .body(transactionService.transaction(userId, transactionDTO));
  }

  // 거래 목록 출력(전체 / 카테고리별)
  @GetMapping("/transaction")
  public ResponseEntity<?> getTransactionList(
      @RequestParam(name = "categoryName", required = false) String categoryName,
      @PageableDefault(page = 0, size = 10) Pageable page,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }
    return ResponseEntity.ok(transactionService.getTransactionList(userId, categoryName, page));
  }

  @PatchMapping("/transaction")
  public ResponseEntity<?> updateTransaction(
      @RequestBody @Valid UpdateTransactionDTO updateTransactionDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(transactionService.updateTransaction(userId, updateTransactionDTO));
  }
}
