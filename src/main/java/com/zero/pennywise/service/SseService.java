package com.zero.pennywise.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {

  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter createEmitter(String userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    emitters.put(userId, emitter);

    emitter.onCompletion(() -> emitters.remove(userId));
    emitter.onTimeout(() -> {
      emitters.remove(userId);
      retryConnection(userId);
    });
    emitter.onError(e -> emitters.remove(userId));

    sendEventToClient(userId, "connect", "connect!!");
    return emitter;
  }

  private void retryConnection(String userId) {
    SseEmitter newEmitter = createEmitter(userId);

    emitters.put(userId, newEmitter);

    try {
      newEmitter.send(SseEmitter.event().name("retry").data("Reconnected"));
    } catch (IOException e) {
      newEmitter.completeWithError(e);
    }
  }

  public void sendEventToClient(String userId, String eventName, String message) {
    SseEmitter emitter = emitters.get(userId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name(eventName).data(message));
      } catch (IOException e) {
        emitters.remove(userId);
        retryConnection(userId);
      }
    }
  }
}
