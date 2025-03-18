package com.zero.pennywise.component.facade;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.budget.Budget;
import com.zero.pennywise.model.response.transaction.TotalAmount;
import com.zero.pennywise.model.response.transaction.Transactions;
import com.zero.pennywise.model.response.waring.MessageDTO;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.NotificationType;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoryRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.TransactionQueryRepository;
import com.zero.pennywise.utils.FormatUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FinanceFacade {

  private final CategoryRepository categoryRepository;
  private final BudgetRepository budgetRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final TransactionRepository transactionRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  // ================================================================ Category ================================================================

  /**
   * 특정 사용자의 모든 카테고리 목록 조회.
   *
   * @param user 카테고리를 조회할 사용자 엔티티
   * @return 사용자의 카테고리 이름 목록 (존재하지 않을 경우 빈 리스트)
   */
  public List<String> getCategoryList(UserEntity user) {
    return categoryRepository.findAllByUser(user)
        .map(categoryList -> categoryList.stream()
            .map(CategoryEntity::getCategoryName)
            .collect(Collectors.toList()))
        .orElse(new ArrayList<>());
  }

  /**
   * 카테고리 생성 전 중복 여부 검증.
   *
   * @param user 카테고리를 생성하려는 사용자 엔티티
   * @param categoryName 검증할 카테고리 이름
   * @throws GlobalException 이미 존재하는 카테고리일 경우 예외 발생
   */
  public void validateCategory(UserEntity user, String categoryName) {
    // 동일한 카테고리명이 이미 존재하는지 확인
    if(categoryRepository.existsByUserAndCategoryName(user, categoryName)) {
      throw new GlobalException(FailedResultCode.CATEGORY_ALREADY_USED);
    }
  }

  /**
   * 카테고리를 생성 및 저장.
   *
   * @param user 카테고리를 생성하는 사용자 엔티티
   * @param categoryName 생성할 카테고리 이름
   */
  public void createAndSaveCategory(UserEntity user, String categoryName) {
    // 새로운 카테고리 엔티티 생성 및 저장
    categoryRepository.save(
        CategoryEntity.builder()
            .user(user)
            .categoryName(categoryName)
            .build());
  }

  /**
   * 특정 사용자의 카테고리 삭제.
   *
   * @param user 카테고리를 삭제하는 사용자 엔티티
   * @param categoryName 삭제할 카테고리 이름
   */
  public void deleteCategory(UserEntity user, String categoryName) {
    // 사용자와 카테고리명으로 카테고리 삭제
    categoryRepository.deleteByUserAndCategoryName(user, categoryName);
  }

  /**
   * 특정 사용자가 입력한 카테고리와 일치하는 CategoryEntity 조회.
   *
   * @param user 카테고리를 조회할 사용자 엔티티
   * @param categoryName 조회할 카테고리 이름
   * @throws GlobalException 존재하지 않은 카테고리일 경우 예외 발생
   */
  public CategoryEntity findCategory(UserEntity user, String categoryName) {
    return categoryRepository.findByUserAndCategoryName(user, categoryName)
        .orElseThrow(() -> new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));
  }


  // ================================================================ Budget ================================================================

  /**
   * 현재 사용자의 모든 예산 목록 조회.
   *
   * @param user 예산을 조회할 사용자 엔티티
   * @return 사용자의 예산 목록 (존재하지 않을 경우 빈 리스트)
   */
  public List<Budget> getBudgetList(UserEntity user) {
    return budgetRepository.findAllByUser(user)
        .map(budgetList -> budgetList.stream()
            .map(Budget::of)
            .collect(Collectors.toList()))
        .orElse(new ArrayList<>());
  }

  /**
   * 동일한 카테고리의 예산 존재 여부 검증.
   *
   * @param user 예산을 생성하려는 사용자 엔티티
   * @param category 검증할 카테고리 엔티티
   * @throws GlobalException 이미 해당 카테고리에 예산이 존재할 경우 예외 발생
   */
  public void validateBudget(UserEntity user, CategoryEntity category) {
    if(budgetRepository.existsByUserAndCategory(user, category)) {
      throw new GlobalException(FailedResultCode.BUDGET_ALREADY_USED);
    }
  }

  /**
   * 새로운 예산을 생성 생성 및 저장.
   *
   * @param user 예산을 생성하는 사용자 엔티티
   * @param category 예산에 연결된 카테고리 엔티티
   * @param budgetDTO 생성할 예산 정보를 담은 DTO
   */
  public void createAndSaveBudget(UserEntity user, CategoryEntity category, BudgetDTO budgetDTO) {
    budgetRepository.save(BudgetDTO.of(user, category, budgetDTO));
  }

  /**
   * 사용자와 카테고리에 해당하는 예산 조회.
   *
   * @param user 예산을 조회할 사용자 엔티티
   * @param category 조회할 카테고리 엔티티
   * @return 조회된 예산 엔티티
   * @throws GlobalException 해당 예산이 존재하지 않을 경우 예외 발생
   */
  public BudgetEntity findBudget(UserEntity user, CategoryEntity category) {
    return budgetRepository.findByUserAndCategory(user, category)
        .orElseThrow(() -> new GlobalException(FailedResultCode.BUDGET_NOT_FOUND));
  }

  /**
   * 기존 예산의 금액을 수정.
   *
   * @param budget 업데이트할 예산 엔티티
   * @param amount 새로 설정할 예산 금액
   */
  public void updateBudget(BudgetEntity budget, Long amount) {
    budgetRepository.updateBudget(budget.getBudgetId(), amount);
  }

  /**
   * 특정 사용자의 예산 삭제.
   *
   * @param budget 삭제할 예산 엔티티
   */
  public void deleteBudget(BudgetEntity budget) {
    budgetRepository.delete(budget);
  }

  public void checkBudget(TransactionEntity transaction) {
    BudgetEntity budget = budgetRepository.findByUserAndCategory(
        transaction.getUser(), transaction.getCategory())
        .orElse(null);

    if(budget != null && budget.getAmount() < transaction.getTotalExpensesAmount()) {
      String overAmount = FormatUtil.formatWon(transaction.getTotalExpensesAmount() - budget.getAmount());
      String message = FormatUtil.formatOverBudgetMessage(transaction.getCategory().getCategoryName(), overAmount);

      MessageDTO messageDTO = new MessageDTO(
          transaction.getUser().getUserId(),
          NotificationType.OVER_BUDGET,
          message
      );
      redisTemplate.convertAndSend("notifications", messageDTO);
    }
  }


  // ================================================================ Transaction ================================================================

  /**
   * 사용자의 거래 내역을 조회.
   *
   * @param user 거래 내역을 조회할 사용자 엔티티
   * @param transactionInfo 조회 조건을 담은 DTO
   * @return 페이징 처리된 거래 내역 응답
   */
  public List<Transactions> getTransactionList(UserEntity user, TransactionInfoDTO transactionInfo) {
    return transactionQueryRepository.getTransactionList(user, transactionInfo);
  }

  /**
   * 새로운 거래 내역 생성 및 저장.
   *
   * @param user 거래를 생성하는 사용자 엔티티
   * @param category 거래와 연결된 카테고리 엔티티
   * @param transactionDTO 생성할 거래 정보를 담은 DTO
   */
  public TransactionEntity createAndSaveTransaction(UserEntity user, CategoryEntity category, TransactionDTO transactionDTO) {
    // 사용자와 카테고리의 현재 총 금액 조회
    TotalAmount totalAmount = transactionQueryRepository.getTotalAmount(user, category);

    // 거래 엔티티 생성 및 저장
    return transactionRepository.save(TransactionDTO.of(user, category, transactionDTO, totalAmount));
  }

  /**
   * 사용자와 카테고리에 해당하는 예산 조회.
   *
   * @param user 예산을 조회할 사용자 엔티티
   * @param transactionId 조회할 거래정보 ID
   * @return 조회된 거래 정보 엔티티
   * @throws GlobalException 해당 거래 내역이 존재하지 않을 경우 예외 발생
   */
  public TransactionEntity findTransaction(UserEntity user, Long transactionId) {
    return transactionRepository.findByUserAndTransactionId(user, transactionId)
        .orElseThrow(() -> new GlobalException(FailedResultCode.TRANSACTION_NOT_FOUND));
  }

  /**
   * 사용자의 특정 거래 내역 삭제.
   *
   * @param transaction 삭제할 거래 정보 엔티티
   */
  public void deleteTransaction(TransactionEntity transaction) {

    transactionRepository.delete(transaction);
  }
}

































