package com.zero.pennywise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.pennywise.model.response.waring.WaringMessageDTO;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements MessageListener {

  private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final ObjectMapper mapper = new ObjectMapper();
  private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

  public SseEmitter createEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    emitters.put(userId, emitter);

    emitter.onCompletion(() -> emitters.remove(userId));
    emitter.onTimeout(() -> emitters.remove(userId));

    sendNotification(userId, "connect!");
    return emitter;
  }

  public void sendNotification(Long userId, String message) {
    SseEmitter emitter = emitters.get(userId);

    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name("notifications").data(message));
      } catch (IOException e) {
        emitters.remove(userId);
      }
    } else {
      logger.info("emitters.get(userId): {}", emitters.get(userId));
      emitters.remove(userId);
    }
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      WaringMessageDTO waringMessage = mapper.readValue(message.getBody(), WaringMessageDTO.class);

      logger.info("waringMessage.toString(): {}", waringMessage.toString());
      sendNotification(waringMessage.getUserId(), waringMessage.getMessage());
    } catch (IOException e) {
      logger.info("Exception {}", e.getMessage());
    }
  }

}