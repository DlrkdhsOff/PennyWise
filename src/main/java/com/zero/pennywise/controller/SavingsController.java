package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.service.SavingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SavingsController {

  private final SavingsService savingsService;

  @PostMapping("/savings")
  public ResponseEntity<?> setSavings(@RequestBody @Valid SavingsDTO savings,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");
    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.ok()
        .body(savingsService.setSabings(userId, savings));
  }


}
