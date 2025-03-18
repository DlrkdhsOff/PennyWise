package com.zero.pennywise.api.service.impl;

import com.zero.pennywise.api.component.FinanceFacade;
import com.zero.pennywise.api.component.UserFacade;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.type.SuccessResultCode;
import com.zero.pennywise.api.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 카테고리 서비스의 구현체.
 * 사용자 인증과 재무 관련 작업을 처리하는 퍼사드를 활용하여
 * 카테고리 관련 비즈니스 로직을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final UserFacade userFacade;
  private final FinanceFacade financeFacade;

  /**
   * 현재 사용자의 카테고리 목록을 조회합니다.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @return 사용자의 카테고리 목록을 포함한 ResultResponse
   */
  @Override
  public ResultResponse getCategoryList(HttpServletRequest request) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    List<String> response = financeFacade.getCategoryList(user);

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, response);
  }

  /**
   * 새로운 카테고리를 생성합니다.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param categoryName 생성할 카테고리의 이름
   * @return 카테고리 생성 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse createCategory(HttpServletRequest request, String categoryName) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    financeFacade.validateCategory(user, categoryName);
    financeFacade.createAndSaveCategory(user, categoryName);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_CATEGORY);
  }

  /**
   * 기존 카테고리를 삭제합니다.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param categoryName 삭제할 카테고리의 이름
   * @return 카테고리 삭제 결과를 포함한 ResultResponse
   */
  @Override
  public ResultResponse deleteCategory(HttpServletRequest request, String categoryName) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    financeFacade.deleteCategory(user, categoryName);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_CATEGORY);
  }
}