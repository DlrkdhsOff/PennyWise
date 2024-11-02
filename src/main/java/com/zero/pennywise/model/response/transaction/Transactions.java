package com.zero.pennywise.model.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

  private Long transactionId;
  private String type;
  private String categoryName;
  private Long amount;
  private String description;
  private String dateTime;
}
