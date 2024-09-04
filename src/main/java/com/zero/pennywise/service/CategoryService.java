package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedData;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
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
  public String createCategory(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);
    List<Categories> categories = categoryCache.getCategoriesFromCache(user.getId());

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


  // 카테고리 수정
  @Transactional
  public String updateCategory(Long userId, UpdateCategoryDTO updateCategoryDTO) {
    UserEntity user = getUserById(userId);
    String beforeCategoryName = updateCategoryDTO.getCategoryName();
    String newCategoryName = updateCategoryDTO.getNewCategoryName();

    List<Categories> categoriesList = categoryCache.getCategoriesFromCache(user.getId());
    CategoriesEntity beforeCategory = categoriesRepository.findByCategoryName(beforeCategoryName);

    // 수정한 카테고리
    CategoriesEntity newCategory = validateCategoryUpdate(user, categoriesList, beforeCategory, newCategoryName);

    categoryCache.updateCategory(categoriesList, user.getId(), beforeCategoryName, newCategory);

    // 나머지 데이터 수정
    updateOther(user.getId(), beforeCategory.getCategoryId(), newCategory.getCategoryId());

    return "성공적으로 카테고리를 수정하였습니다.";
  }


  // 카테고리 삭제
  @Transactional
  public String deleteCategory(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);
    List<Categories> categoriesList = categoryCache.getCategoriesFromCache(user.getId());

    Categories categories = categoryCache.getCategory(categoriesList, categoryName);

    userCategoryRepository
        .deleteAllByUserIdAndCategoryCategoryId(user.getId(), categories.getCategoryId());

    // 아무도 사용하지 않는 카테고리는 categories 테이블에서도 삭제
    if (!isCategoryUsedByOtherUser(user, categories.getCategoryId())) {
      categoriesRepository.deleteByCategoryId(categories.getCategoryId());
    }

    categoryCache.deleteCategory(categoriesList, user.getId(), categoryName);

    return "성공적으로 카테고리를 삭제하였습니다.";
  }




  // 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
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


  // 유효값 검증 및 캐시 데이터 수정
  private CategoriesEntity validateCategoryUpdate(UserEntity user, List<Categories> categoriesList,
      CategoriesEntity category, String newCategoryName) {

    if (category == null || !categoryCache.isCategoryNameExists(categoriesList, category.getCategoryName())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다.");
    }

    // 새 카테고리가 이미 존재하는지 확인
    if (categoryCache.isCategoryNameExists(categoriesList, newCategoryName)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }

    // 다른 사용자가 이 카테고리를 사용 중인지 확인하고 처리
    if (isCategoryUsedByOtherUser(user, category.getCategoryId())) {
      category = categoriesRepository.findByCategoryName(newCategoryName);

      if (category == null) {
        category = createNewCategory(newCategoryName);
      }
    } else {
      category.setCategoryName(newCategoryName); // 사용 중이지 않으면 이름 업데이트
    }

    categoriesRepository.save(category);

    return category;
  }


  // 다른 사용자가 카테고리를 사용하는지 확인
  private boolean isCategoryUsedByOtherUser(UserEntity user, Long categoryId) {
    return userCategoryRepository
        .existsByUserNotAndCategoryCategoryId(user, categoryId
        );
  }


  // 사용자 카테고리, 거래, 예산 카테고리 id 변경
  private void updateOther(Long userId, Long categoryId, Long newCategoryId) {
    categoryQueryRepository
        .updateCategory(userId, categoryId, newCategoryId);

    transactionQueryRepository
        .updateCategory(userId, categoryId, newCategoryId);

    budgetQueryRepository
        .updateCategory(userId, categoryId, newCategoryId);
  }
}