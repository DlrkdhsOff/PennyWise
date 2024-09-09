package com.zero.pennywise.controller;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.service.SavingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/savings")
  public ResponseEntity<?> savings(
      @PageableDefault(page = 0, size = 10) Pageable page,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");
    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.ok().body(savingsService.getSavings(userId, page(page)));
  }
}
