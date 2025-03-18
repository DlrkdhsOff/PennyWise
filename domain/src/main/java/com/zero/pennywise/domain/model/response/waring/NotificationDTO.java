package com.zero.pennywise.domain.model.response.waring;

import com.zero.pennywise.domain.entity.NotificationEntity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

  private String message;

  private String createAt;

  public static List<NotificationDTO> of(List<NotificationEntity> notificationList) {
    return notificationList.stream()
        .map(notification -> new NotificationDTO(
            notification.getMessage(),
            notification.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
        .collect(Collectors.toList());
  }
}
