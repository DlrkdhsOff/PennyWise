package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.service.AnalyzeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<?> analyzeIncomeAndExpenses(HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");
    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.ok()
        .body(analyzeService.analyze(userId));
  }


}
