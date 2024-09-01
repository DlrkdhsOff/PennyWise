package com.zero.pennywise.model.response;

import java.util.Map;
import lombok.Data;

@Data
public class UserBalanceDTO {

  private Long amount;
  private Map<String, Long> categoryAmount;
}
