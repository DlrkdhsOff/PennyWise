package com.zero.pennywise.api.service;

import com.zero.pennywise.domain.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService extends MessageListener {

  SseEmitter createEmitter(HttpServletRequest request);

  ResultResponse getNotificationList(HttpServletRequest request, int page);
}
