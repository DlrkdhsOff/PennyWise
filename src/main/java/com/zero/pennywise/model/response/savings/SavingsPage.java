package com.zero.pennywise.model.response.savings;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class SavingsPage {

  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<SavingsDataDTO> categories;

  public static SavingsPage of(Page<SavingsDataDTO> pageList) {
    return new SavingsPage(
        pageList.getNumber() + 1,
        pageList.getTotalPages(),
        pageList.getTotalElements(),
        pageList.getContent()
    );

  }


}
