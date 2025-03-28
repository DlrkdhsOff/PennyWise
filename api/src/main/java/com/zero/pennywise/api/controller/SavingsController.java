//package com.zero.pennywise.api.controller;
//
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.api.service.SavingsService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/savings")
//public class SavingsController {
//
//  private final SavingsService savingsService;
//
//  @PostMapping("/recommend")
//  public ResponseEntity<ResultResponse> savingInfo(HttpServletRequest request) {
//
//    ResultResponse resultResponse = savingsService.recommend(request);
//    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
//  }
//}
