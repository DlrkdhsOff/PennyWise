package com.zero.pennywise.component;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.model.response.waring.MessageDTO;
import com.zero.pennywise.repository.WaringMessageRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaringMessageHandler {

  private final WaringMessageRepository waringMessageRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  public void sendMessage(UserEntity user, String message) {
    WaringMessageEntity warningMessage = WaringMessageEntity.builder()
        .user(user)
        .message(message)
        .recivedDateTime(LocalDateTime.now())
        .build();

    waringMessageRepository.save(warningMessage);
    redisTemplate.convertAndSend("notifications", new MessageDTO(user.getUserId(), message));
  }

}
