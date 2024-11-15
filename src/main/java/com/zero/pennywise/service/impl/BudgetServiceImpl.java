package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.handler.BudgetHandler;
import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.TokenType;
import com.zero.pennywise.service.BudgetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

  private final JwtUtil jwtUtil;
  private final UserHandler userHandler;
  private final BudgetHandler budgetHandler;
  private final CategoryHandler categoryHandler;

  // 요청 헤더에서 사용자 정보를 추출하여 반환
  private UserEntity fetchUser(HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    return userHandler.findByUserId(userId);
  }

  // 사용자와 카테고리 이름으로 카테고리 엔티티 조회
  private CategoryEntity fetchCategory(UserEntity user, String categoryName) {
    return categoryHandler.findCategory(user, categoryName);
  }

  // 예산 목록 조회
  @Override
  public ResultResponse getBudget(String categoryName, int page, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    PageResponse<Budgets> response = PageResponse.of(budgetHandler.getBudgetInfo(user, categoryName), page);
    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, response);
  }

  // 새로운 예산 등록
  @Override
  public ResultResponse createBudget(BudgetDTO budgetDTO, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    CategoryEntity category = fetchCategory(user, budgetDTO.getCategoryName());

    // 예산 중복 검증
    budgetHandler.validateCreateBudget(user, category);

    // 예산 엔티티 생성 및 저장
    BudgetEntity budget = BudgetDTO.of(user, category, budgetDTO);
    budgetHandler.saveBudget(budget);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_BUDGET);
  }

  // 기존 예산 수정
  @Transactional
  public ResultResponse updateBudget(UpdateBudgetDTO updateBudgetDTO, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    CategoryEntity category = fetchCategory(user, updateBudgetDTO.getCategoryName());

    // 예산 중복 검증
    budgetHandler.validateUpdateBudget(user, category);

    // 수정된 예산 정보 저장
    BudgetEntity newBudget = UpdateBudgetDTO.of(user, category, updateBudgetDTO);
    budgetHandler.saveBudget(newBudget);

    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_BUDGET);
  }

  // 예산 삭제
  @Override
  @Transactional
  public ResultResponse deleteBudget(Long budgetId, HttpServletRequest request) {
    UserEntity user = fetchUser(request);

    // 예산 존재 여부 검증 및 삭제
    BudgetEntity budget = budgetHandler.findBudgetById(budgetId, user);
    budgetHandler.deleteBudget(budget);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_BUDGET);
  }
}