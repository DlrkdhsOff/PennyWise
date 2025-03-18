package com.zero.pennywise.model.response.waring;

import com.zero.pennywise.entity.NotificationEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

  private UserEntity user;

  private NotificationType notificationType;

  private String message;


  public static NotificationEntity of(MessageDTO message) {
    return NotificationEntity.builder()
        .user(message.getUser())
        .notificationType(message.getNotificationType())
        .message(message.getMessage())
        .build();
  }
}
