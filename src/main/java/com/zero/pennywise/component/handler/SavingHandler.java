package com.zero.pennywise.component.handler;

import static com.zero.pennywise.enums.TransactionStatus.getEnumType;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.enums.TransactionStatus;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.repository.CategoryRepository;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SavingHandler {

  private final SavingsRepository savingsRepository;
  private final CategoryRepository categoryRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final TransactionRepository transactionRepository;

  public void save(UserEntity user, SavingsDTO savingsDTO) {
    try {
      SavingsEntity savings = savingsRepository.save(SavingsEntity.builder()
          .user(user)
          .category(getCategory(user))
          .name(savingsDTO.getName())
          .amount(savingsDTO.getAmount())
          .startDate(savingsDTO.getStartDate())
          .endDate(savingsDTO.getStartDate().plusMonths(savingsDTO.getMonthsToSave()))
          .description(savingsDTO.getDescription())
          .build());

      if (savingsDTO.getStartDate().equals(LocalDate.now())) {
        transactionRepository.save(TransactionEntity.builder()
            .user(user)
            .categoryId(savings.getCategory().getCategoryId())
            .type(TransactionStatus.FIXED_EXPENSES)
            .amount(savings.getAmount())
            .description(savings.getName() + savings.getDescription())
            .dateTime(LocalDateTime.now())
            .build());
      }
    } catch (Exception e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 저축명 입니다.");
    }
  }



  public CategoryEntity getCategory(UserEntity user) {
    return categoryRepository.findByUserIdAndCategoryName(user.getId(), "저축")
        .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
            .user(user)
            .categoryName("저축")
            .build()));
  }

  public SavingsEntity getSavings(UserEntity user, String name) {
    return savingsRepository.findByUserIdAndName(user.getId(), name)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 저축 정보 입니다."));
  }


  @Transactional
  public void endDeposit(UserEntity user, SavingsEntity savings) {

    CategoryEntity category = getCategory(user);
    String description = savings.getName() + savings.getDescription();

    // 삭제 후 같은 이름으로 저축을 생성했을 경우 이전 기록이 조회 되기 때문에
    // 해당 저축 정보의 type를 END로 변경
    transactionQueryRepository
        .endSavings(user.getId(), category.getCategoryId(), description);

    // 저축 종료시 모은 금액 거래 목록에 수입으로 저장
    Long currentAmount = transactionQueryRepository
        .getCurrentAmount(user, category.getCategoryId(), description);

    if (currentAmount > 0) {
      transactionRepository.save(TransactionEntity.builder()
          .user(user)
          .categoryId(category.getCategoryId())
          .type(TransactionStatus.INCOME)
          .amount(currentAmount)
          .description(description)
          .dateTime(LocalDateTime.now())
          .build());
    }

    savingsRepository.deleteById(savings.getId());
  }

  public List<SavingsEntity> getAllSavings(Long userId) {
    return savingsRepository.findAllByUserId(userId);
  }
}
