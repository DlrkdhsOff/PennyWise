package com.zero.pennywise.component.facade;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 재무 관련 작업을 처리하는 퍼사드 컴포넌트.
 * 카테고리 관리를 위한 비즈니스 로직을 캡슐화합니다.
 */
@Component
@RequiredArgsConstructor
public class FinanceFacade {

  // 카테고리 데이터 접근을 위한 리포지토리
  private final CategoryRepository categoryRepository;

  /**
   * 특정 사용자의 모든 카테고리 목록을 조회합니다.
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
   * 카테고리 생성 전 중복 여부를 검증합니다.
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
   * 새로운 카테고리를 생성하고 저장합니다.
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
   * 특정 사용자의 카테고리를 삭제합니다.
   *
   * @param user 카테고리를 삭제하는 사용자 엔티티
   * @param categoryName 삭제할 카테고리 이름
   */
  public void deleteCategory(UserEntity user, String categoryName) {
    // 사용자와 카테고리명으로 카테고리 삭제
    categoryRepository.deleteByUserAndCategoryName(user, categoryName);
  }
}