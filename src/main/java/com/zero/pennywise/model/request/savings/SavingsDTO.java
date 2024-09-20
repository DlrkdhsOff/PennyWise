package com.zero.pennywise.model.request.savings;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingsDTO {

  @NotBlank(message = "저축명을 입력해주세요.")
  private String name;

  @NotNull(message = "금액을 입력해주세요.")
  private Long amount;

  @NotBlank(message = "설명을 입력해주세요.")
  private String description;

  @NotNull(message = "저축 시작 날짜를 입력해주세요.")
  @FutureOrPresent(message = "지난 날짜는 입력 할 수 없습니다.")
  private LocalDate startDate;

  @Min(value = 3, message = "최소 3개월 부터 등록 할 수 있습니다.")
  private int monthsToSave;

}
