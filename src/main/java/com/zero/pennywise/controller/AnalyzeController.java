//package com.zero.pennywise.controller;
//
//import com.zero.pennywise.service.AnalyzeService;
//import com.zero.pennywise.utils.UserAuthorizationUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//public class AnalyzeController {
//
//  private final AnalyzeService analyzeService;
//
//  @GetMapping("/analyze")
//  public ResponseEntity<?> analyzeIncomeAndExpenses() {
//
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.ok()
//        .body(analyzeService.analyze(userId));
//  }
//
//
//}