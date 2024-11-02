//package com.zero.pennywise.service.impl;
//
//import com.zero.pennywise.auth.jwt.JwtUtil;
//import com.zero.pennywise.component.handler.BudgetHandler;
//import com.zero.pennywise.component.handler.CategoryHandler;
//import com.zero.pennywise.component.handler.RedisHandler;
//import com.zero.pennywise.component.handler.UserHandler;
//import com.zero.pennywise.entity.BudgetEntity;
//import com.zero.pennywise.entity.CategoryEntity;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.model.request.budget.BudgetDTO;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.model.response.balances.Balances;
//import com.zero.pennywise.model.response.page.PageResponse;
//import com.zero.pennywise.model.type.SuccessResultCode;
//import com.zero.pennywise.model.type.TokenType;
//import com.zero.pennywise.repository.BudgetRepository;
//import com.zero.pennywise.service.BudgetService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class BudgetServiceImpl implements BudgetService {
//
//  private final JwtUtil jwtUtil;
//  private final UserHandler userHandler;
////  private final RedisHandler redisHandler;
//  private final BudgetHandler budgetHandler;
//  private final CategoryHandler categoryHandler;
//
//  // 예산 목록
//  @Override
//  public ResultResponse getBudget(int page, HttpServletRequest request) {
//    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
//    UserEntity user = userHandler.findByUserId(userId);
//
//    PageResponse<Balances> response = redisHandler.getBalance(user, page);
//    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, response);
//  }
//
//  // 카테고리별 예산 설정
//  @Override
//  public ResultResponse createBudget(BudgetDTO budgetDTO, HttpServletRequest request) {
//
//    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
//    UserEntity user = userHandler.findByUserId(userId);
//
//    CategoryEntity category = categoryHandler.findCategory(user, budgetDTO.getCategoryName());
//
//    budgetHandler.validateBudget(user, category);
//    BudgetEntity budget = BudgetEntity.builder()
//        .user(user)
//        .category(category)
//        .amount(budgetDTO.getAmount())
//        .build();
//
//    budgetHandler.saveBudget(budget);
//
//    redisHandler.addNewBudget(user.getId(), budget, category.getCategoryName());
//    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_BUDGET);
//  }
//
//  // 카테고리별 예산 수정
//  @Transactional
//  public ResultResponse updateBudget(BudgetDTO budgetDTO, HttpServletRequest request) {
//    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
//    UserEntity user = userHandler.findByUserId(userId);
//
//    CategoryEntity category = categoryHandler.findCategory(user, budgetDTO.getCategoryName());
//
//    BudgetEntity budget = budgetHandler.findBudget(user, category);
//    redisHandler.updateBudget(user.getId(), budget.getAmount(), budgetDTO.getAmount(), budgetDTO.getCategoryName());
//
//    BudgetEntity newBudget = BudgetEntity.builder()
//        .budgetId(budget.getBudgetId())
//        .user(budget.getUser())
//        .category(category)
//        .amount(budgetDTO.getAmount())
//        .build();
//
//    budgetHandler.saveBudget(newBudget);
//
//    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_BUDGET);
//  }
//
//  // 예산 삭제
//  @Override
//  @Transactional
//  public ResultResponse deleteBudget(String categoryName, HttpServletRequest request) {
//    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
//    UserEntity user = userHandler.findByUserId(userId);
//
//    CategoryEntity category = categoryHandler.findCategory(user, categoryName);
//    BudgetEntity budget = budgetHandler.findBudget(user, category);
//
//    budgetHandler.deleteBudget(budget);
//
//    redisHandler.deleteBalance(userId, category.getCategoryName());
//    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_BUDGET);
//  }
//}