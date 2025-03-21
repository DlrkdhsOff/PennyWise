package com.zero.pennywise.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.pennywise.core.service.NotificationService;
import com.zero.pennywise.core.component.UserFacade;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.response.page.PageResponse;
import com.zero.pennywise.domain.model.response.waring.MessageDTO;
import com.zero.pennywise.domain.model.response.waring.NotificationDTO;
import com.zero.pennywise.domain.model.type.SuccessResultCode;
import com.zero.pennywise.domain.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final UserFacade userFacade;
  private final NotificationRepository notificationRepository;
  private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final ObjectMapper mapper = new ObjectMapper();


  public SseEmitter createEmitter(HttpServletRequest request) {

    UserEntity user = userFacade.getUserByAccessToken(request);

    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    emitters.put(user.getUserId(), emitter);

    emitter.onCompletion(() -> emitters.remove(user.getUserId()));
    emitter.onTimeout(() -> emitters.remove(user.getUserId()));

    sendNotification(user.getUserId(), "connect!");
    return emitter;
  }

  public void sendNotification(Long userId, String message) {
    SseEmitter emitter = emitters.get(userId);

    if (emitter != null) {
      try {
        log.info("==================================");
        emitter.send(SseEmitter.event().name("notifications").data(message));
      } catch (IOException e) {
        emitters.remove(userId);
      }
    } else {
      log.info("emitters.get(userId): {}", emitters.get(userId));
      emitters.remove(userId);
    }
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      MessageDTO messageDTO = mapper.readValue(message.getBody(), MessageDTO.class);

      log.info("message: {}", messageDTO.getMessage());
      sendNotification(messageDTO.getUserId(), messageDTO.getMessage());
      saveNotification(messageDTO);
    } catch (IOException e) {
      log.info("Exception {}", e.getMessage());
    }
  }

  private void saveNotification(MessageDTO messageDTO) {
    UserEntity user = userFacade.findByUserId(messageDTO.getUserId());
    notificationRepository.save(MessageDTO.of(user, messageDTO));
  }

  @Override
  public ResultResponse getNotificationList(HttpServletRequest request, int page) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    List<NotificationDTO> notificationList = notificationRepository.findAllByUser(user)
        .map(NotificationDTO::of)
        .orElse(new ArrayList<>());

    PageResponse<NotificationDTO> response = PageResponse.of(notificationList, page);

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_WARING_MESSAGE, response);
  }

}