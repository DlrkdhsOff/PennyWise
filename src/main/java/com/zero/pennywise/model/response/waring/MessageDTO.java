package com.zero.pennywise.model.response.waring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zero.pennywise.entity.NotificationEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.type.NotificationType;
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
