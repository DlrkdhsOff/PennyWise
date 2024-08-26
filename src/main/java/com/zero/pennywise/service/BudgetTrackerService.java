package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.BudgetDTO;
import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.model.response.TransactionList;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.view.V_TransactionRepository;
import com.zero.pennywise.status.BudgetTrackerStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetTrackerService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;
  private final TransactionRepository transactionRepository;
  private final V_TransactionRepository v_transactionRepository;

  // 카테고리 목록
  public List<String> getCategoryList(Long userId) {
    List<BudgetEntity> budgetList = budgetRepository.findAllByUserId(userId);

    List<String> categoryList = new ArrayList<>();

    for (BudgetEntity budget : budgetList) {
      categoriesRepository.findById(budget.getCategoryId())
          .ifPresent(category -> categoryList.add(category.getCategoryName()));
    }

    return categoryList;
  }

  // 카테고리 생성
  public Response createCategory(Long userId, CategoryDTO categoryDTO) {
    return categoriesRepository.findByCategoryName(categoryDTO.getCategoryName())
        .map(category -> existingCategory(userId, category))
        .orElseGet(() -> createNewCategory(userId, categoryDTO));
  }

  // 카테고리 존재 여부 확인
  private Response existingCategory(Long userId, CategoriesEntity category) {
    // 사용자가 이미 등록한 카테고리일 경우
    if (budgetRepository.existsByUserIdAndCategoryId(userId, category.getCategoryId())) {
      return new Response(BudgetTrackerStatus.CATEGORY_ALREADY_EXISTS);
    }

    // category 테이블에는 존재하는 카테고리이지만 해당 사용자가 등록한 카테고리가 아닐경우
    createBudget(userId, category);
    return new Response(BudgetTrackerStatus.SUCCESS_CREATE_CATEGORY);
  }

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  private Response createNewCategory(Long userId, CategoryDTO categoryDTO) {
    CategoriesEntity category = categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryDTO.getCategoryName())
            .shared(false)
            .build()
    );

    createBudget(userId, category);
    return new Response(BudgetTrackerStatus.SUCCESS_CREATE_CATEGORY);
  }

  // 기본 예산 생성 메서드
  private void createBudget(Long userId, CategoriesEntity category) {
    budgetRepository.save(BudgetEntity.builder()
        .userId(userId)
        .categoryId(category.getCategoryId())
        .build());
  }


  // 카테고리별 예산 설정
  public Response setBudget(Long userId, BudgetDTO budgetDTO) {
    return categoriesRepository.findByCategoryName(budgetDTO.getCategoryName())

        // 사용자가 등록한 카테고리 예산 설정
        .map(category -> {
          BudgetEntity budget = budgetRepository
              .findByUserIdAndCategoryId(userId, category.getCategoryId());

          budget.setAmount(budgetDTO.getAmount());
          budgetRepository.save(budget);

          return new Response(BudgetTrackerStatus.SUCCESS_SET_BUDGET);
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElse(new Response(BudgetTrackerStatus.CATEGORY_NOT_FOUND));
  }

  // 수입/지출 등록
  public Response transaction(Long userId, TransactionDTO transactionDTO) {
    return categoriesRepository.findByCategoryName(transactionDTO.getCategoryName())
        .map(category -> {
          transactionRepository.save(
              TransactionDTO.of(userId, category.getCategoryId(), transactionDTO)
          );

          return new Response(BudgetTrackerStatus.SUCCESS_TRANSACTION_REGISTRATION);
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElse(new Response(BudgetTrackerStatus.CATEGORY_NOT_FOUND));
  }

  // 수입 / 지출 내역
  public Object getTransactionList(Long userId, String categoryName) {
    // 전체 거래 내역 / 카테고리별 거래 내역
    List<TransactionList> transactions = (categoryName == null || categoryName.isBlank())
        ? TransactionList.of(v_transactionRepository.findAllByUserId(userId))
        : TransactionList.of(v_transactionRepository.findAllByUserIdAndCategoryName(userId, categoryName));

    if (transactions.isEmpty()) {
      return new Response(categoryName != null
          ? BudgetTrackerStatus.CATEGORY_NOT_FOUND
          : BudgetTrackerStatus.TRANSACTIONS_NOT_FOUND);
    }
    return transactions;
  }

}