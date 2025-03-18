package com.zero.pennywise.domain.model.response.waring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zero.pennywise.domain.model.type.NotificationType;
import com.zero.pennywise.domain.entity.NotificationEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {

  private Long userId;

  private NotificationType notificationType;

  private String message;


  public static NotificationEntity of(UserEntity user, MessageDTO message) {
    return NotificationEntity.builder()
        .user(user)
        .notificationType(message.getNotificationType())
        .message(message.getMessage())
        .build();
  }
}
