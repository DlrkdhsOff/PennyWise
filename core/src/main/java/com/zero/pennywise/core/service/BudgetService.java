package com.zero.pennywise.core.service;

import com.zero.pennywise.domain.model.request.budget.BudgetDTO;
import com.zero.pennywise.domain.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface BudgetService {

  ResultResponse getBudgetList(HttpServletRequest request);

  ResultResponse createBudget(HttpServletRequest request, BudgetDTO budgetDTO);

  ResultResponse updateBudget(HttpServletRequest request, BudgetDTO budgetDTO);

  ResultResponse deleteBudget(HttpServletRequest request, String categoryName);

//  ResultResponse getBudget(String categoryName, int page, HttpServletRequest request);
//
//  ResultResponse createBudget(BudgetDTO budgetDTO, HttpServletRequest request);
//
//  ResultResponse updateBudget(UpdateBudgetDTO updateBudgetDTO, HttpServletRequest request);
//
//  ResultResponse deleteBudget(HttpServletRequest request, Long budgetId);
}
