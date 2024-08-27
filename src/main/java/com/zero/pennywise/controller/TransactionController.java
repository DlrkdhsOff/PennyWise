package com.zero.pennywise.controller;

import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    Response result = transactionService.transaction(userId, transactionDTO);

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

  // 거래 목록 출력(전체 / 카테고리별)
  @GetMapping("/transaction")
  public ResponseEntity<?> getTransactionList(
      @RequestParam(name = "categoryName", required = false) String categoryName,
      @RequestParam(name = "page", required = false) String page,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    return ResponseEntity.ok().body(transactionService.getTransactionList(userId, categoryName, page));
  }
}
