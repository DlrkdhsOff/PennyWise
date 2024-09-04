package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedData;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.CategoryDTO;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.Categories;
import com.zero.pennywise.model.response.CategoriesPage;
import com.zero.pennywise.redis.CategoryCache;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
import com.zero.pennywise.repository.querydsl.category.CategoryQueryRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final UserCategoryRepository userCategoryRepository;
  private final CategoriesRepository categoriesRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final UserRepository userRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final BudgetQueryRepository budgetQueryRepository;
  private final CategoryCache categoryCache;

  // 카테고리 목록
  public CategoriesPage getCategoryList(Long userId, Pageable pageable) {
    List<Categories> cachedData = categoryCache.getCategoriesFromCache(userId);

    // 캐시에 데이터가 있으면 캐시 데이터를 반환
    if (cachedData != null) {
      return CategoriesPage.of(getPagedData(cachedData, pageable));
    }

    List<Categories> categories = categoryQueryRepository.getAllCategory(userId, pageable);
    categoryCache.putCategoriesInCache(userId, categories);

    return CategoriesPage.of(getPagedData(categories, pageable));
  }

  // 카테고리 생성
  public String createCategory(Long userId, CategoryDTO categoryDTO) {
    UserEntity user = getUserById(userId);

    return categoriesRepository.findByCategoryName(categoryDTO.getCategoryName())
        .map(category -> existingCategory(user, category))
        .orElseGet(() -> createNewCategory(user, categoryDTO));
  }

  // 카테고리 존재 여부 확인
  private String existingCategory(UserEntity user, CategoriesEntity category) {
    List<Categories> categories = categoryCache.getCategoriesFromCache(user.getId());
    if (isCategoryNameExists(categories, category.getCategoryName())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }

    categoryCache.putNewCategoryInCache(categories, user.getId(), category);
    saveUserCategory(category, user);
    return "카테고리를 생성하였습니다.";
  }

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  private String createNewCategory(UserEntity user, CategoryDTO categoryDTO) {
    CategoriesEntity category = categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryDTO.getCategoryName())
            .build()
    );

    List<Categories> categories = categoryCache.getCategoriesFromCache(user.getId());
    categoryCache.putNewCategoryInCache(categories, user.getId(), category);

    saveUserCategory(category, user);
    return "카테고리를 생성하였습니다.";
  }

  // 카테고리 수정
  @Transactional
  public String updateCategoryName(Long userId, UpdateCategoryDTO updateCategoryDTO) {
    UserEntity user = getUserById(userId);
    CategoriesEntity existingCategory = getCategoryByName(updateCategoryDTO.getCategoryName());

    boolean isUsedByOtherUser = isCategoryUsedByOtherUser(user, existingCategory);
    CategoriesEntity updatedCategory = updateOrCreateNewCategory(
        user.getId(),
        existingCategory,
        updateCategoryDTO.getNewCategoryName(),
        isUsedByOtherUser
    );

    // 카테고리 정보 수정
    categoryQueryRepository.updateCategory(
        user.getId(),
        existingCategory.getCategoryId(),
        updatedCategory
    );

    // 거래 정보 수정
    transactionQueryRepository.updateCategoryId(
        user.getId(),
        existingCategory.getCategoryId(),
        updatedCategory
    );

    // 예산 정보 수정
    budgetQueryRepository.updateCategoryId(
        user.getId(),
        existingCategory.getCategoryId(),
        updatedCategory
    );

    return "성공적으로 카테고리를 수정하였습니다.";
  }

  // 카테고리 업데이트 또는 새로운 카테고리 생성
  private CategoriesEntity updateOrCreateNewCategory(
      Long userId,
      CategoriesEntity currentCategory,
      String newCategoryName,
      boolean isUsedByOtherUser
  ) {
    return categoriesRepository.findByCategoryName(newCategoryName)
        .map(category -> {
          if (userCategoryRepository.existsByUserIdAndCategoryCategoryId(userId,
              category.getCategoryId())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
          }
          return category;
        })
        .orElseGet(() -> {
          if (isUsedByOtherUser) {
            return categoriesRepository.save(CategoriesEntity.builder()
                .categoryName(newCategoryName)
                .build()
            );
          } else {
            currentCategory.setCategoryName(newCategoryName);
            return categoriesRepository.save(currentCategory);
          }
        });
  }

  // 카테고리 삭제
  @Transactional
  public String deleteCategory(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);
    CategoriesEntity existingCategory = getCategoryByName(categoryName);

    boolean isUsedByOtherUser = isCategoryUsedByOtherUser(user, existingCategory);

    if (!isUsedByOtherUser) {
      categoriesRepository.deleteByCategoryId(existingCategory.getCategoryId());
    }
    userCategoryRepository.deleteAllByUserIdAndCategoryCategoryId(
        user.getId(),
        existingCategory.getCategoryId()
    );

    return "성공적으로 카테고리를 삭제하였습니다.";
  }

  // 공통 메서드

  // 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  // 사용자 카테고리 저장
  public void saveUserCategory(CategoriesEntity category, UserEntity user) {
    userCategoryRepository.save(
        UserCategoryEntity.builder()
            .category(category)
            .user(user)
            .build()
    );
  }

  // 해당 카테고리가 존재하는지 확인
  public boolean isCategoryNameExists(List<Categories> categories, String categoryName) {
    for (Categories category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        return true;
      }
    }
    return false;
  }

  // 카테고리 조회
  private CategoriesEntity getCategoryByName(String categoryName) {
    return categoriesRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."));
  }

  // 다른 사용자가 카테고리를 사용하는지 확인
  private boolean isCategoryUsedByOtherUser(UserEntity user, CategoriesEntity category) {
    return userCategoryRepository.existsByUserNotAndCategoryCategoryId(
        user,
        category.getCategoryId()
    );
  }
}