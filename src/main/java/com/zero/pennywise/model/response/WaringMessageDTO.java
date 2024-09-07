package com.zero.pennywise.model.response;

import com.zero.pennywise.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaringMessageDTO {

  private UserEntity user;
  private String message;

}
