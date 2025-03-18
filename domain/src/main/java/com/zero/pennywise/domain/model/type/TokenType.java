package com.zero.pennywise.domain.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
  ACCESS("access"),
  REFRESH("refresh");

  private final String value;
}
