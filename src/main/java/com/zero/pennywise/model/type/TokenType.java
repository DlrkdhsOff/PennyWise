package com.zero.pennywise.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
  ACCESS("access"),
  REFRESH("refresh");

  private final String value;
}
