package com.zero.pennywise.service.impl;

import com.zero.pennywise.component.facade.FacadeManager;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.service.BudgetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

  private final FacadeManager facadeManager;


  // 예산 목록 조회
  @Override
  public ResultResponse getBudget(String categoryName, int page, HttpServletRequest request) {
    PageResponse<Budgets> response = facadeManager.getBudgetList(request, categoryName, page);

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_BUDGET_LIST, response);
  }

  // 새로운 예산 등록
  @Override
  public ResultResponse createBudget(BudgetDTO budgetDTO, HttpServletRequest request) {
    BudgetEntity budget = facadeManager.createBudget(request, budgetDTO);


    facadeManager.saveBudget(budget);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_BUDGET);
  }

  // 기존 예산 수정
  @Transactional
    public ResultResponse updateBudget(UpdateBudgetDTO updateBudgetDTO, HttpServletRequest request) {
    BudgetEntity budget = facadeManager.updateBudget(request, updateBudgetDTO);

    facadeManager.saveBudget(budget);

    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_BUDGET);
  }

  // 예산 삭제
  @Override
  @Transactional
  public ResultResponse deleteBudget(HttpServletRequest request, Long budgetId) {

    facadeManager.deleteBudget(request, budgetId);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_BUDGET);
  }
}