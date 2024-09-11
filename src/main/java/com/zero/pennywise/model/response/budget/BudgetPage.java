package com.zero.pennywise.model.response.budget;

import com.zero.pennywise.model.request.budget.BalancesDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class BudgetPage {

  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<BalancesDTO> budgetList;

  public static BudgetPage of(Page<BalancesDTO> pageList) {
    return new BudgetPage(
        pageList.getNumber() + 1,
        pageList.getTotalPages(),
        pageList.getTotalElements(),
        pageList.getContent()
    );

  }

}
