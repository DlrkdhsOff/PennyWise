//package com.zero.pennywise.component.facade;
//
//import com.zero.pennywise.entity.BudgetEntity;
//import com.zero.pennywise.entity.CategoryEntity;
//import com.zero.pennywise.entity.TransactionEntity;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.exception.GlobalException;
//import com.zero.pennywise.model.request.budget.BudgetDTO;
//import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
//import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
//import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
//import com.zero.pennywise.model.response.budget.Budgets;
//import com.zero.pennywise.model.response.page.PageResponse;
//import com.zero.pennywise.model.response.transaction.Transactions;
//import com.zero.pennywise.model.type.FailedResultCode;
//import com.zero.pennywise.repository.BudgetRepository;
//import com.zero.pennywise.repository.CategoryRepository;
//import com.zero.pennywise.repository.TransactionRepository;
//import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
//import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class FinanceFacade {
//
//  private final CategoryRepository categoryRepository;
//  private final TransactionRepository transactionRepository;
//  private final BudgetRepository budgetRepository;
//
//  private final TransactionQueryRepository transactionQueryRepository;
//  private final BudgetQueryRepository budgetQueryRepository;
//
//  // ================= Cateogry =================
//
//  /**
//   * UserEntity, CategoryName과 일치하는 CategoryEntity 조회
//   *
//   * @param user UserEntity
//   * @param categoryName 조회할 카테고리 명
//   */
//  public CategoryEntity getCategoryByUserAndCategoryName(UserEntity user, String categoryName) {
//    return categoryRepository.findByUserAndCategoryName(user, categoryName)
//        .orElseThrow(() -> new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));
//  }
//
//
//  /**
//   * 카테고리 List 조회
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity가 포함된 Category List 조회
//   * 페이징 처리 후 PageResponse 반환
//   *
//   * @param request Request Header
//   * @param page 보여줄 페이지 번호
//   */
//  public PageResponse<String> getUserCategoryList(HttpServletRequest request, int page) {
//    UserEntity user = getUserByAccessToken(request);
//
//    List<String> categories = categoryRepository.findAllByUser(user)
//        .map(categoryList -> categoryList.stream()
//            .map(CategoryEntity::getCategoryName).toList())
//        .orElseThrow(() -> new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));
//
//    return PageResponse.of(categories, page);
//  }
//
//
//  /**
//   * CategoryEntity 생성
//   * Request Header 값으로 UserEntity 조회
//   * 카테고리 중복 여부 검증
//   * CagegoryEntity 생성 후 반환
//   *
//   * @param request Request Header
//   * @param categoryName 생성할 카테고리 명
//   */
//  public CategoryEntity createCategory(HttpServletRequest request, String categoryName) {
//    UserEntity user = getUserByAccessToken(request);
//
//    if (categoryRepository.existsByUserAndCategoryName(user, categoryName)) {
//      throw new GlobalException(FailedResultCode.CATEGORY_ALREADY_USED);
//    }
//
//    return CategoryEntity.builder()
//        .categoryName(categoryName)
//        .user(user)
//        .build();
//  }
//
//
//  /**
//   * 카테고리 수정
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity, CategoryName과 일치하는 CategoryEntity 조회
//   * 수정할 카테고리명 중복 여부 검증
//   * 수정한 CategoryEntity 반환
//   *
//   * @param request Request Header
//   * @param updateCategoryDTO 수정할 카테고리 정보
//   */
//  public CategoryEntity updateCategory(HttpServletRequest request, UpdateCategoryDTO updateCategoryDTO) {
//    UserEntity user = getUserByAccessToken(request);
//
//    CategoryEntity category = getCategoryByUserAndCategoryName(user,
//        updateCategoryDTO.getBeforecategoryName());
//
//    if(categoryRepository.existsByUserAndCategoryName(user, updateCategoryDTO.getAfterCategoryName())) {
//      throw new GlobalException(FailedResultCode.CATEGORY_ALREADY_USED);
//    }
//
//    return UpdateCategoryDTO.of(category, updateCategoryDTO);
//  }
//
//
//  /**
//   * 카테고리 저장
//   *
//   * @param category 저장할 CategoryEntity
//   */
//  public void saveCategory(CategoryEntity category) {
//    categoryRepository.save(category);
//  }
//
//
//  /**
//   * 카테고리 삭제
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity, CategoryName과 일치하는 CategoryEntity 조회
//   * CategoryEntity 삭제
//   *
//   * @param request Request Header
//   * @param categoryName 삭제할 카테고리 명
//   */
//  public void deleteCategory(HttpServletRequest request, String categoryName) {
//    UserEntity user = getUserByAccessToken(request);
//
//    CategoryEntity category = getCategoryByUserAndCategoryName(user, categoryName);
//
//    categoryRepository.delete(category);
//  }
//
//
//  // ================= Transaction =================
//
//
//  /**
//   * 거래 정보 List 조회
//   * Request Header 값으로 UserEntity 조회
//   * QueryDls을 활용하여 조건에 맞는 거래 정보 조회
//   * 페이징 처리 후 PageResponse 반환
//   *
//   * @param request Request Header
//   * @param transactionInfoDTO 조회할 거래 정보
//   * @param page 보여줄 페이지 번호
//   */
//  public PageResponse<Transactions> getTransactionList(HttpServletRequest request, TransactionInfoDTO transactionInfoDTO, int page) {
//    UserEntity user = getUserByAccessToken(request);
//
//    List<Transactions> transactions = transactionQueryRepository.getTransactionInfo(user, transactionInfoDTO);
//
//    return PageResponse.of(transactions, page);
//  }
//
//
//  /**
//   * TransactionEntity 생성
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity, CategoryName과 일치하는 CategoryEntity 조회
//   * TransactionEntity 생성 후 반환
//   *
//   * @param request Request Header
//   * @param transactionDTO 등록할 거래 정보
//   */
//  public TransactionEntity createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) {
//    UserEntity user = getUserByAccessToken(request);
//
//    CategoryEntity category = getCategoryByUserAndCategoryName(user, transactionDTO.getCategoryName());
//
//    return TransactionDTO.of(user, category, transactionDTO);
//  }
//
//
//  /**
//   * TransactionEntity 저장
//   *
//   * @param transaction 저장할 TransactionEntity
//   */
//  public void saveTransaction(TransactionEntity transaction) {
//    transactionRepository.save(transaction);
//  }
//
//
//  /**
//   * TransactionEntity 삭제
//   * transactionId 값과 일치하는 TransactionEntity 조회
//   * 조회한 TransactionEntity 삭제
//   *
//   * @param transactionId 삭제할 Transaction Id값
//   */
//  public void deleteTransaction(HttpServletRequest request, Long transactionId) {
//    UserEntity user = getUserByAccessToken(request);
//
//    TransactionEntity transaction = transactionRepository
//        .findByUserAndTransactionId(user, transactionId)
//        .orElseThrow(() -> new GlobalException(FailedResultCode.TRANSACTION_NOT_FOUND));
//
//    transactionRepository.delete(transaction);
//  }
//
//
//  // ================= Budget =================
//
//
//  /**
//   * 예산 List 조회
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity, CategoryName과 일치하는 BudgetList 조회
//   * 페이징 처리 후 PageResponse 반환
//   *
//   * @param request Request Header
//   * @param categoryName 조회할 예산의 카테고리 명
//   * @param page 보여줄 페이지 번호
//   */
//  public PageResponse<Budgets> getBudgetList(HttpServletRequest request, String categoryName, int page) {
//    UserEntity user = getUserByAccessToken(request);
//
//    List<Budgets> budgets = budgetQueryRepository.getBudgetList(user, categoryName);
//
//    return PageResponse.of(budgets, page);
//  }
//
//
//  /**
//   * BudgetEntity 생성
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity, CategoryName과 일치하는 CategoryEntity 조회
//   * 조회한 정보 및 입력한 정보를 활용하여 BudgetEntity 생성 후 반환
//   *
//   * @param request Request Header
//   * @param budgetDTO 등록할 예산 정보
//   */
//  public BudgetEntity createBudget(HttpServletRequest request, BudgetDTO budgetDTO) {
//    UserEntity user = getUserByAccessToken(request);
//
//    CategoryEntity category = getCategoryByUserAndCategoryName(user, budgetDTO.getCategoryName());
//
//    if (budgetRepository.existsByUserAndCategory(user, category)) {
//      throw new GlobalException(FailedResultCode.BUDGET_ALREADY_USED);
//    }
//
//    return BudgetDTO.of(user, category, budgetDTO);
//  }
//
//  /**
//   * BudgetEntity 저장
//   *
//   * @param budget 저장할 BudgetEntity
//   */
//  public void saveBudget(BudgetEntity budget) {
//    budgetRepository.save(budget);
//  }
//
//
//  /**
//   * BudgetEntity 수정
//   * Request Header 값으로 UserEntity 조회
//   * UserEntity, CategoryName과 일치하는 CategoryEntity 조회
//   * 조회한 정보 및 입력한 정보를 활용하여 BudgetEntity 수정 후 반환
//   *
//   * @param request Request Header
//   * @param updateBudgetDTO 수정할 예산 정보
//   */
//  public BudgetEntity updateBudget(HttpServletRequest request, UpdateBudgetDTO updateBudgetDTO) {
//    UserEntity user = getUserByAccessToken(request);
//
//    CategoryEntity category = getCategoryByUserAndCategoryName(user, updateBudgetDTO.getCategoryName());
//
//    return UpdateBudgetDTO.of(user, category, updateBudgetDTO);
//  }
//
//  /**
//   * BudgetEntity 삭제
//   * budgetId 값과 일치하는 BudgetEntity 조회
//   * 조회한 BudgetEntity 삭제
//   *
//   * @param budgetId 삭제할 BudgetEntity Id 값
//   */
//  public void deleteBudget(HttpServletRequest request, Long budgetId) {
//    UserEntity user = getUserByAccessToken(request);
//
//    BudgetEntity budget = budgetRepository.findByBudgetIdAndUser(budgetId, user)
//        .orElseThrow(() -> new GlobalException(FailedResultCode.BUDGET_NOT_FOUND));
//
//    budgetRepository.delete(budget);
//  }
//
//}
