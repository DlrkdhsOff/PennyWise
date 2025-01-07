package com.zero.pennywise.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.zero.pennywise.component.facade.FacadeManager;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

  @Mock
  private FacadeManager facadeManager;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private CategoryServiceImpl categoryServiceImpl;


  private final int PAGE = 1;
  private final String CATEGORY_NAME = "categoryName";

  private PageResponse<String> pageResponse;
  private CategoryEntity category;
  private UpdateCategoryDTO updateCategoryDTO;

  @BeforeEach
  void setUp() {
    List<String> categories = List.of("카테고리1", "카테고리2");
    pageResponse = PageResponse.of(categories, PAGE);

    UserEntity user = UserEntity.builder()
        .userId(1L)
        .email("email")
        .password("password")
        .nickname("nickname")
        .role(UserRole.USER)
        .build();

    category = CategoryEntity.builder()
        .categoryId(1L)
        .categoryName(CATEGORY_NAME)
        .user(user)
        .build();

    updateCategoryDTO = new UpdateCategoryDTO("beforeCategoryName", "afterCategoryName");
  }


  @Test
  @DisplayName("카테고리 List 조회 : 성공")
  void getCategoryList() {
    // given
    when(facadeManager.getUserCategoryList(request, PAGE)).thenReturn(pageResponse);

    // when
    ResultResponse response = categoryServiceImpl.getCategoryList(PAGE, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("카테고리 List 조회 : 실패 - 존재하지 않은 사용자")
  void getCategoryList_Failed_UserNotFound() {
    // given
    when(facadeManager.getUserCategoryList(request, PAGE)).thenThrow(new GlobalException(
        FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.getCategoryList(PAGE, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 List 조회 : 실패 - 존재하지 않은 카테고리")
  void getCategoryList_Failed_CategoryNotFound() {
    // given
    when(facadeManager.getUserCategoryList(request, PAGE)).thenThrow(new GlobalException(
        FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.getCategoryList(PAGE, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 생성 : 성공")
  void createCategory() {
    // given
    when(facadeManager.createCategory(request, CATEGORY_NAME)).thenReturn(category);

    // when
    ResultResponse response = categoryServiceImpl.createCategory(CATEGORY_NAME, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_CREATE_CATEGORY.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("카테고리 생성 : 실패 - 존재하지 않은 사용자")
  void createCategory_Failed_UserNotFound() {
    // given
    when(facadeManager.createCategory(request, CATEGORY_NAME)).thenThrow(new GlobalException(
        FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.createCategory(CATEGORY_NAME, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 생성 : 실패 - 이미 등록한 카테고리")
  void createCategory_Failed_ExistsCategory() {
    // given
    when(facadeManager.createCategory(request, CATEGORY_NAME)).thenThrow(new GlobalException(
        FailedResultCode.CATEGORY_ALREADY_USED));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.createCategory(CATEGORY_NAME, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 수정 : 성공")
  void updateCategory() {
    // given
    when(facadeManager.updateCategory(request, updateCategoryDTO)).thenReturn(category);

    // when
    ResultResponse response = categoryServiceImpl.updateCategory(updateCategoryDTO, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_UPDATE_CATEGORY.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("카테고리 수정 : 실패 - 존재하지 않은 사용자")
  void updateCategory_Failed_UserNotFound() {
    // given
    when(facadeManager.updateCategory(request, updateCategoryDTO)).thenThrow(new GlobalException(
        FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.updateCategory(updateCategoryDTO, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 수정 : 실패 - 존재하지 않은 카테고리")
  void updateCategory_Failed_CategoryNotFound() {
    // given
    when(facadeManager.updateCategory(request, updateCategoryDTO)).thenThrow(new GlobalException(
        FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.updateCategory(updateCategoryDTO, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 수정 : 실패 - 이미 등록한 카테고리")
  void updateCategory_Failed_ExistsCategory() {
    // given
    when(facadeManager.updateCategory(request, updateCategoryDTO)).thenThrow(new GlobalException(
        FailedResultCode.CATEGORY_ALREADY_USED));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.updateCategory(updateCategoryDTO, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 삭제 : 성공")
  void deleteCategory() {
    // given & when
    ResultResponse response = categoryServiceImpl.deleteCategory(CATEGORY_NAME, request);

    // then
    assertEquals(SuccessResultCode.SUCCESS_DELETE_CATEGORY.getStatus(), response.getStatus());
  }

  @Test
  @DisplayName("카테고리 삭제 : 실패 - 존재하지 않은 사용자")
  void deleteCategory_Failed_UserNotFound() {
    // given
    doThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND))
        .when(facadeManager).deleteCategory(request, CATEGORY_NAME);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.deleteCategory(CATEGORY_NAME, request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 삭제 : 실패 - 존재하지 않은 카테고리")
  void deleteCategory_Failed_Failed_CategoryNotFound() {
    // given
    doThrow(new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND))
        .when(facadeManager).deleteCategory(request, CATEGORY_NAME);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> categoryServiceImpl.deleteCategory(CATEGORY_NAME, request));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }
}