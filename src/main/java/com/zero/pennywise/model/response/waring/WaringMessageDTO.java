package com.zero.pennywise.model.response.waring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaringMessageDTO {

  private Long userId;
  private String message;

}
