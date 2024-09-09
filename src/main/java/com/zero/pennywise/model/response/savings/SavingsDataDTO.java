package com.zero.pennywise.model.response.savings;

import com.zero.pennywise.entity.UserEntity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingsDataDTO {

  private String name;

  private Long amount;

  private Long currentAmount;

  private String description;

  private LocalDate startDate;

  private LocalDate endDate;

  private LocalDate createDate;
}
