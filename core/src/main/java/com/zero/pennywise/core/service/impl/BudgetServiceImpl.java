package com.zero.pennywise.core.service.impl;

import com.zero.pennywise.core.service.BudgetService;
import com.zero.pennywise.core.component.FinanceFacade;
import com.zero.pennywise.core.component.UserFacade;
import com.zero.pennywise.domain.entity.BudgetEntity;
import com.zero.pennywise.domain.entity.CategoryEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.request.budget.BudgetDTO;
import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.response.budget.Budget;
import com.zero.pennywise.domain.model.type.SuccessResultCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

  private final UserFacade userFacade;
  private final FinanceFacade financeFacade;

  /**
   * 현재 사용자의 모든 예산 목록 조회.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @return 사용자의 예산 목록과 성공 코드를 포함한 ResultResponse
   */
  @Override
  public ResultResponse getBudgetList(HttpServletRequest request) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    List<Budget> budgetList = financeFacade.getBudgetList(user);
    return new ResultResponse(SuccessResultCode.SUCCESS_GET_BUDGET_LIST, budgetList);
  }

  /**
   * 예산을 생성.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param budgetDTO 생성할 예산의 정보를 담은 DTO
   * @return 예산 생성 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse createBudget(HttpServletRequest request, BudgetDTO budgetDTO) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    // 사용자의 특정 카테고리 조회
    CategoryEntity category = financeFacade.findCategory(user, budgetDTO.getCategoryName());

    // 예산 생성 전 검증 및 저장
    financeFacade.validateBudget(user, category);
    financeFacade.createAndSaveBudget(user, category, budgetDTO);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_BUDGET);
  }

  /**
   * 기존 예산 수정.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param budgetDTO 수정할 예산 정보를 담은 DTO
   * @return 예산 수정 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse updateBudget(HttpServletRequest request, BudgetDTO budgetDTO) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    // 사용자의 특정 카테고리 및 해당 카테고리의 예산 조회
    CategoryEntity category = financeFacade.findCategory(user, budgetDTO.getCategoryName());
    BudgetEntity budget = financeFacade.findBudget(user, category);

    // 예산 금액 업데이트
    financeFacade.updateBudget(budget, budgetDTO.getAmount());
    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_BUDGET);
  }

  /**
   * 특정 카테고리의 예산 삭제
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param categoryName 삭제할 예산의 카테고리 이름
   * @return 예산 삭제 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse deleteBudget(HttpServletRequest request, String categoryName) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    // 사용자의 특정 카테고리 및 해당 카테고리의 예산 조회
    CategoryEntity category = financeFacade.findCategory(user, categoryName);
    BudgetEntity budget = financeFacade.findBudget(user, category);

    // 예산 삭제
    financeFacade.deleteBudget(budget);
    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_BUDGET);
  }

}