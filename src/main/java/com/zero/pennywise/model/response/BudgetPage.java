package com.zero.pennywise.model.response;

import com.zero.pennywise.model.request.budget.BudgetDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class BudgetPage {

  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<BudgetDTO> budgetList;

  public static BudgetPage of(Page<BudgetDTO> pageList) {
    return new BudgetPage(
        pageList.getNumber() + 1,
        pageList.getTotalPages(),
        pageList.getTotalElements(),
        pageList.getContent()
    );

  }

}
