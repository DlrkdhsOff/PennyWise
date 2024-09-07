package com.zero.pennywise.model.response.waring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaringMessageDTO {

  private Long userId;
  private String message;

}
