package com.zero.pennywise.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

  OVER_BUDGET("예산 초과"),
  HIGH_SPENDING("높은 지출")
  ;

  private final String type;
}
