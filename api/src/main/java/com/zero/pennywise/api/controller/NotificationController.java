package com.zero.pennywise.api.controller;

import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.api.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscribe")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(HttpServletRequest request) {

    // 새로운 SseEmitter 생성
    return notificationService.createEmitter(request);
  }

  @GetMapping("/message")
  public ResponseEntity<ResultResponse> getNotificationList(HttpServletRequest request,
      @RequestParam("page") int page) {

    ResultResponse response = notificationService.getNotificationList(request, page);
    return new ResponseEntity<>(response, response.getStatus());
  }
}