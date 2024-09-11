package com.zero.pennywise.model.request.savings;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingsDTO {

  private String name;

  private Long amount;

  private String description;

  private LocalDate startDate;

  private int monthsToSave;

}
