package com.zero.pennywise.service;

import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetTrackerService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;

  public List<CategoriesEntity> getCategoryList(Long userId) {
    List<BudgetEntity> budgetList = budgetRepository.findAllByUserId(userId);

    List<CategoriesEntity> categoryList = new ArrayList<>();

    for (BudgetEntity budget : budgetList) {
      Optional<CategoriesEntity> optional = categoriesRepository.findById(budget.getCategoryId());
      optional.ifPresent(categoryList::add);
    }

    return categoryList;
  }

}
