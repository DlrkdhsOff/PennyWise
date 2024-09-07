package com.zero.pennywise.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService  {
  private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter createEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 무한대 타임아웃 설정
    emitters.put(userId, emitter);

    emitter.onCompletion(() -> emitters.remove(userId)); // 연결 완료 시 제거
    emitter.onTimeout(() -> emitters.remove(userId)); // 타임아웃 발생 시 처리
    emitter.onError(e -> emitters.remove(userId)); // 에러 발생 시 처리

    sendEventToClient(userId, "connect", "Connected!!");
    return emitter;
  }

  public void sendEventToClient(Long userId, String eventName, String message) {
    SseEmitter emitter = emitters.get(userId);
    if (emitter != null) {
      try {
        System.out.println("Emitter found for userId: " + userId + " -> " + emitters.containsKey(userId));

        emitter.send(SseEmitter.event().name(eventName).data(message));
      } catch (IOException e) {
        System.out.println("Failed to send message to emitter for userId: " + userId + " due to: " + e.getMessage());
        emitters.remove(userId); // 메시지 전송 실패 시 처리
      }
    }
    System.out.println("userId = " + userId);
  }

}