//package com.zero.pennywise.controller;
//
//import com.zero.pennywise.model.request.transaction.TransactionDTO;
//import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.service.TransactionService;
//import com.zero.pennywise.utils.UserAuthorizationUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/transaction")
//public class TransactionController {
//
//  private final TransactionService transactionService;
//
//  // 거래 목록 출력 (전체)
//  @GetMapping
//  public ResponseEntity<ResultResponse> getTransactionList(
//      @RequestParam("page") int page,
//      HttpServletRequest request)
//  {
//
//    ResultResponse resultResponse = transactionService.getAllTransactionList(page, request);
//    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
//  }
//
//  // 수입 / 지출 등록
//  @PostMapping
//  public ResponseEntity<String> transaction(
//      @RequestBody @Valid TransactionDTO transactionDTO)
//  {
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.status(HttpStatus.CREATED)
//        .body(transactionServiceImpl.transaction(userId, transactionDTO));
//  }
//
//  // 거래 수정
//  @PatchMapping("/transaction")
//  public ResponseEntity<String> updateTransaction(
//      @RequestBody @Valid UpdateTransactionDTO updateTransactionDTO)
//  {
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.status(HttpStatus.OK)
//        .body(transactionServiceImpl.updateTransaction(userId, updateTransactionDTO));
//  }
//
//  // 거래 삭제
//  @DeleteMapping("/transaction")
//  public ResponseEntity<String> deleteTransaction(
//      @RequestParam(name = "trasactionId", required = false) Long trasactionId)
//  {
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.ok()
//        .body(transactionServiceImpl.deleteTransaction(userId, trasactionId));
//  }
//}