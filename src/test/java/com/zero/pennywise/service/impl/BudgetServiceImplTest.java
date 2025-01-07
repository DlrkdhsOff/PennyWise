package com.zero.pennywise.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.zero.pennywise.component.facade.FacadeManager;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

  @Mock
  private FacadeManager facadeManager;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private BudgetServiceImpl budgetServiceImpl;

  private final int PAGE = 1;
  private final String CATEGORY_NAME = "categoryName";
  private final Long BUDGET_ID = 1L;

  private PageResponse<Budgets> pageResponse;
  private BudgetDTO budgetDTO;
  private BudgetEntity budget;
  private UpdateBudgetDTO updateBudgetDTO;

  @BeforeEach
  void setUp() {
    List<Budgets> budgets = List.of(
        new Budgets(1L, "카테고리1", 1000L, 3000L),
        new Budgets(2L, "카테고리2", 2000L, 5000L)
    );

    pageResponse = PageResponse.of(budgets, PAGE);

    budgetDTO = new BudgetDTO(CATEGORY_NAME, 1000L);

    UserEntity user = UserEntity.builder()
        .userId(1L)
        .email("email")
        .password("password")
        .nickname("nickname")
        .role(UserRole.USER)
        .build();

    CategoryEntity category = CategoryEntity.builder()
        .categoryId(1L)
        .categoryName(CATEGORY_NAME)
        .user(user)
        .build();

    budget = BudgetEntity.builder()
        .budgetId(BUDGET_ID)
        .user(user)
        .category(category)
        .amount(1000L)
        .build();

    updateBudgetDTO = new UpdateBudgetDTO(BUDGET_ID, CATEGORY_NAME, 3000L);
  }

  @Test
  @DisplayName("예산 목록 조회 : 성공")
  void getBudget() {
    // given
    when(facadeManager.getBudgetList(request, CATEGORY_NAME, PAGE)).thenReturn(pageResponse);

    // when
    ResultResponse response = budgetServiceImpl.getBudget(CATEGORY_NAME, PAGE, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_GET_BUDGET_LIST.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("예산 목록 조회 : 실패 - 존재하지 않은 사용자")
  void getBudget_Failed_UserNotFound() {
    // given
    when(facadeManager.getBudgetList(request, CATEGORY_NAME, PAGE))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.getBudget(CATEGORY_NAME, PAGE, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 등록 : 성공")
  void createBudget() {
    // given
    when(facadeManager.createBudget(request, budgetDTO)).thenReturn(budget);

    // when
    ResultResponse response = budgetServiceImpl.createBudget(budgetDTO, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_CREATE_BUDGET.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("예산 등록 : 실패 - 존재하지 않은 사용자")
  void createBudget_Failed_UserNotFoundd() {
    // given
    when(facadeManager.createBudget(request, budgetDTO))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.createBudget(budgetDTO, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 등록 : 실패 - 존재하지 않은 카테고리")
  void createBudget_Failed_CategoryNotFound() {
    // given
    when(facadeManager.createBudget(request, budgetDTO))
        .thenThrow(new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.createBudget(budgetDTO, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 등록 : 실패 - 이미 등록한 예산")
  void createBudget_Failed_ExistsBudget() {
    // given
    when(facadeManager.createBudget(request, budgetDTO))
        .thenThrow(new GlobalException(FailedResultCode.BUDGET_ALREADY_USED));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.createBudget(budgetDTO, request));

    // then
    assertEquals(
        FailedResultCode.BUDGET_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 수정 : 성공")
  void updateBudget() {
    // given
    when(facadeManager.updateBudget(request, updateBudgetDTO)).thenReturn(budget);

    // when
    ResultResponse response = budgetServiceImpl.updateBudget(updateBudgetDTO, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_UPDATE_BUDGET.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("예산 수정 : 실패 - 존재하지 않은 사용자")
  void updateBudget_Failed_UserNotFoundd() {
    // given
    when(facadeManager.updateBudget(request, updateBudgetDTO))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.updateBudget(updateBudgetDTO, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 수정 : 실패 - 존재하지 않은 카테고리")
  void updateBudget_Failed_CategoryNotFound() {
    // given
    when(facadeManager.updateBudget(request, updateBudgetDTO))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.updateBudget(updateBudgetDTO, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 삭제 : 성공")
  void deleteBudget() {
    // given & when
    ResultResponse response = budgetServiceImpl.deleteBudget(request, BUDGET_ID);

    // then
    assertEquals(SuccessResultCode.SUCCESS_DELETE_BUDGET.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("예산 삭제 : 실패 - 존재하지 않은 사용자")
  void deleteBudget_Failed_UserNotFound() {
    // given
    doThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND))
        .when(facadeManager).deleteBudget(request, BUDGET_ID);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.deleteBudget(request, BUDGET_ID));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("예산 삭제 : 실패 - 존재하지 않은 카테고리")
  void deleteBudget_Failed_CategoryNotFound() {
    // given
    doThrow(new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND))
        .when(facadeManager).deleteBudget(request, BUDGET_ID);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> budgetServiceImpl.deleteBudget(request, BUDGET_ID));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }
}