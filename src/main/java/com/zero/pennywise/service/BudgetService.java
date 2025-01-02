package com.zero.pennywise.service;

import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface BudgetService {

  ResultResponse getBudget(String categoryName, int page, HttpServletRequest request);

  ResultResponse createBudget(BudgetDTO budgetDTO, HttpServletRequest request);

  ResultResponse updateBudget(UpdateBudgetDTO updateBudgetDTO, HttpServletRequest request);

  ResultResponse deleteBudget(Long budgetId);
}
