package com.zero.pennywise.service;

import com.zero.pennywise.component.handler.SavingHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.component.scheduler.TransactionScheduler;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingsService {

  private final UserHandler userHandler;
  private final SavingHandler savingHandler;
  private final TransactionScheduler transactionScheduler;

  public String setSabings(Long userId, SavingsDTO savingsDTO) {
    UserEntity user = userHandler.getUserById(userId);

    SavingsEntity savings = savingHandler.save(user, savingsDTO);
    CategoriesEntity category = savingHandler.getOrCreateCategory(user);
    transactionScheduler.schedulePayment(user, category, savings);

    return "성공적으로 저축 정보를 등록하였습니다. ";
  }

}
