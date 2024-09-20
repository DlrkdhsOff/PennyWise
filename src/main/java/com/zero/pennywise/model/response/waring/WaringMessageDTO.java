package com.zero.pennywise.model.response.waring;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaringMessageDTO {

  private String message;
  private String recivedDateTime;
}
