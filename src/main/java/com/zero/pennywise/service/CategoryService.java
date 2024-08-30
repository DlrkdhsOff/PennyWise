package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.category.CategoryDTO;
import com.zero.pennywise.model.dto.category.UpdateCategoryDTO;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.entity.UserCategoryEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.CategoriesPage;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.CategoryQueryRepository;
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

  // 카테고리 목록
  public CategoriesPage getCategoryList(Long userId, Pageable page) {
    Pageable pageable = page(page);

    return CategoriesPage
        .of(categoryQueryRepository.getAllCategory(userId, pageable));
  }

  // 카테고리 생성
  public String createCategory(Long userId, CategoryDTO categoryDTO) {

    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."));

    return categoriesRepository.findByCategoryName(categoryDTO.getCategoryName())
        .map(category -> existingCategory(user, category))
        .orElseGet(() -> createNewCategory(user, categoryDTO));
  }

  // 카테고리 존재 여부 확인
  private String existingCategory(UserEntity user, CategoriesEntity category) {
    // 사용자가 이미 등록한 카테고리일 경우
    if (userCategoryRepository.existsByUserIdAndCategoryCategoryId(user.getId(),
        category.getCategoryId())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }

    userCategoryRepository.save(UserCategoryEntity.builder()
        .category(category)
        .user(user)
        .build());
    return "카테고리를 생성하였습니다.";
  }

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  private String createNewCategory(UserEntity user, CategoryDTO categoryDTO) {
    CategoriesEntity category = categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryDTO.getCategoryName())
            .build()
    );

    userCategoryRepository.save(UserCategoryEntity.builder()
        .category(category)
        .user(user)
        .build());

    return "카테고리를 생성하였습니다.";
  }

  // 카테고리 수정
  @Transactional
  public String updateCategoryName(Long userId, UpdateCategoryDTO updateCategoryDTO) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

    CategoriesEntity existingCategory = categoriesRepository.findByCategoryName(
            updateCategoryDTO.getCategoryName())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."));

    boolean isUsedByOtherUser = userCategoryRepository
        .existsByUserNotAndCategoryCategoryId(user, existingCategory.getCategoryId());

    CategoriesEntity updatedCategory = UpdateNewCategory(user.getId(), existingCategory,
        updateCategoryDTO.getNewCategoryName(), isUsedByOtherUser);

    categoryQueryRepository.updateCategory(user.getId(), existingCategory.getCategoryId(),
        updatedCategory);

    return "성공적으로 카테고리를 수정하였습니다.";
  }

  private CategoriesEntity UpdateNewCategory(Long userId, CategoriesEntity currentCategory,
      String newCategoryName, boolean isUsedByOtherUser) {
    return categoriesRepository.findByCategoryName(newCategoryName)
        .map(category -> {
          // 수정할 카테고리가 해당 사용자가 이미 생성한 카테고리 일 경우
          if (userCategoryRepository.existsByUserIdAndCategoryCategoryId(userId,
              category.getCategoryId())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
          }
          return category;
        })
        .orElseGet(() -> {
          if (isUsedByOtherUser) {
            // 다른 사용자가 사용 중인 경우, 새로운 카테고리 생성
            return categoriesRepository.save(CategoriesEntity.builder()
                .categoryName(newCategoryName)
                .build());
          } else {
            // 기존 카테고리 이름 변경
            currentCategory.setCategoryName(newCategoryName);
            return categoriesRepository.save(currentCategory);
          }
        });
  }
}