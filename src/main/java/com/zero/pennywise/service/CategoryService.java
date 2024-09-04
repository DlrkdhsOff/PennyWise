package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedData;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
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
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);

    if (categories == null) {
      categories = getCategories(userId);
    }

    return CategoriesPage.of(getPagedData(categories, pageable));
  }

  // 카테고리 생성
  public String createCategory(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(user.getId());
    if (categories == null) {
      categories = getCategories(userId);
    }
    if (categoryCache.isCategoryNameExists(categories, categoryName)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }

    CategoriesEntity newCategory = categoriesRepository.findByCategoryName(categoryName);

    if (newCategory == null) {
      newCategory = createNewCategory(categoryName);
    }

    categoryCache.putNewCategoryInCache(categories, user.getId(), newCategory);
    saveUserCategory(newCategory, user);

    return "카테고리를 생성하였습니다.";
  }

  @Transactional
  public String updateCategory(Long userId, UpdateCategoryDTO updateCategoryDTO) {
    UserEntity user = getUserById(userId);
    String beforeCategoryName = updateCategoryDTO.getCategoryName();
    String newCategoryName = updateCategoryDTO.getNewCategoryName();
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);

    if (categories == null) {
      categories = getCategories(userId);
    }

    CategoriesEntity beforeCategory = getCategory(categories, user.getId(),
        beforeCategoryName, newCategoryName);

    CategoriesEntity newCategory = updateOrCreateCategory(user, beforeCategory, newCategoryName);

    updateOther(user.getId(), beforeCategory.getCategoryId(), newCategory.getCategoryId());

    // 캐시 업데이트
    categoryCache.updateCategory(categories, user.getId(), beforeCategoryName, newCategory);

    return "카테고리를 성공적으로 수정하였습니다.";
  }

  // 카테고리 삭제
  @Transactional
  public String deleteCategory(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(user.getId());

    if (categories == null) {
      categories = getCategories(userId);
    }

    CategoriesEntity category = categoryCache.getCategory(categories, categoryName);

    userCategoryRepository.deleteAllByUserIdAndCategoryCategoryId(user.getId(), category.getCategoryId());

    // 아무도 사용하지 않는 카테고리는 categories 테이블에서도 삭제
    if (!isCategoryUsedByOtherUser(user, category.getCategoryId())) {
      categoriesRepository.deleteByCategoryId(category.getCategoryId());
    }

    categoryCache.deleteCategory(categories, user.getId(), categoryName);

    return "성공적으로 카테고리를 삭제하였습니다.";
  }

  // 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  // 사용자의 모든 카테고리 정보를 캐시에 저장
  private List<CategoriesEntity> getCategories(Long userId) {
    List<CategoriesEntity> categories = categoryQueryRepository.getAllCategory(userId);
    categoryCache.putCategoriesInCache(userId, categories);
    return categories;
  }

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  private CategoriesEntity createNewCategory(String categoryName) {
    return categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryName)
            .build()
    );
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

  // 기존 카테고리 가져오기
  private CategoriesEntity getCategory(List<CategoriesEntity> categories,
      Long userId, String categoryName, String newCategoryName) {
    CategoriesEntity category = categoryCache.getCategory(categories, categoryName);
    if (category == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다.");
    }
    if (categoryCache.isCategoryNameExists(categories, newCategoryName)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 등록한 카테고리 입니다.");
    }
    return category;
  }

  // 새 카테고리 또는 기존 카테고리 업데이트
  private CategoriesEntity updateOrCreateCategory(UserEntity user,
      CategoriesEntity beforeCategory, String newCategoryName) {
    CategoriesEntity newCategory = categoriesRepository.findByCategoryName(newCategoryName);

    // 기존 카테고리가 다른 사용자가 사용 중이면 새 카테고리를 생성
    if (isCategoryUsedByOtherUser(user, beforeCategory.getCategoryId())) {
      if (newCategory == null) {
        newCategory = createNewCategory(newCategoryName);
      }
    } else {
      // 새 카테고리가 없다면 새로 생성
      if (newCategory == null) {
        beforeCategory.setCategoryName(newCategoryName);
        newCategory = categoriesRepository.save(beforeCategory);
      } else {
        // 새로운 카테고리가 이미 존재하면 기존 카테고리 삭제
        categoriesRepository.deleteByCategoryId(beforeCategory.getCategoryId());
      }
    }
    return newCategory;
  }

  // 다른 사용자가 카테고리를 사용하는지 확인
  private boolean isCategoryUsedByOtherUser(UserEntity user, Long categoryId) {
    return userCategoryRepository.existsByUserNotAndCategoryCategoryId(user, categoryId);
  }

  // 사용자 카테고리, 거래, 예산 카테고리 id 변경
  private void updateOther(Long userId, Long categoryId, Long newCategoryId) {
    categoryQueryRepository.updateCategory(userId, categoryId, newCategoryId);
    transactionQueryRepository.updateCategory(userId, categoryId, newCategoryId);
    budgetQueryRepository.updateCategory(userId, categoryId, newCategoryId);
  }

}