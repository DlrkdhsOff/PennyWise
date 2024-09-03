package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

  private final SseService sseService;

  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return sseService.createEmitter(userId.toString());
  }
}
