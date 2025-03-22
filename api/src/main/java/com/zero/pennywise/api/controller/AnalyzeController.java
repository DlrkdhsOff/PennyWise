package com.zero.pennywise.api.controller;


import com.zero.pennywise.core.service.AnalyzeService;
import com.zero.pennywise.domain.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnalyzeController {

  private final AnalyzeService analyzeService;

  @GetMapping("/analyze")
  public ResponseEntity<ResultResponse> analyzeIncomeAndExpenses(HttpServletRequest request) {

    ResultResponse response = analyzeService.analyzeIncomeAndExpenses(request);

    return new ResponseEntity<>(response, response.getStatus());
  }


}