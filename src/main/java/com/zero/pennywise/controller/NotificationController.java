//package com.zero.pennywise.controller;
//
//import com.zero.pennywise.service.NotificationService;
//import com.zero.pennywise.utils.UserAuthorizationUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//public class NotificationController {
//
//  private final NotificationService notificationService;
//
//  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//  public SseEmitter subscribe() {
//
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    // 새로운 SseEmitter 생성
//    return notificationService.createEmitter(userId);
//  }
//}