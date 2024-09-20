package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.entity.redis.BalanceEntity;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.model.response.waring.MessageDTO;
import com.zero.pennywise.repository.RedisRepository;
import com.zero.pennywise.repository.WaringMessageRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHandler {

  private final RedisRepository redisRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final WaringMessageRepository waringMessageRepository;
  private final RedisTemplate<String, Object> redisTemplate;


  // 예산 목록 조회
  public List<BalancesDTO> getBalance(Long userId) {
    BalanceEntity balanceEntity = redisRepository.findByUserId(userId.toString());
    return balanceEntity.getBalances();
  }

  // 생성한 예산 등록
  public void addNewBudget(Long userId, BudgetEntity budget, String categoryName) {
    BalanceEntity balanceEntity = redisRepository.findByUserId(userId.toString());

    List<BalancesDTO> list = balanceEntity.getBalances();

    Long totalExpenses = transactionQueryRepository
        .getExpenses(userId, budget.getCategory().getCategoryId(), LocalDate.now().toString());

    Long amount = budget.getAmount();

    Long balance = amount - totalExpenses;

    list.add(new BalancesDTO(categoryName, amount, balance));
    balanceEntity.setBalances(list);
    redisRepository.save(balanceEntity);
  }

  public void updateCategoryName(Long userId, String beforeCategoryName, String afterCategoryName) {
    BalanceEntity balanceEntity = redisRepository.findByUserId(userId.toString());

    for (BalancesDTO dto : balanceEntity.getBalances()) {
      if (dto.getCategoryName().contains(beforeCategoryName)) {
        dto.setCategoryName(afterCategoryName);
      }
    }

    redisRepository.save(balanceEntity);
  }


  // 예산 수정
  public void updateBudget(Long userId, Long beforeAmount, Long amount, String categoryName) {
    BalanceEntity balanceEntity = redisRepository.findByUserId(userId.toString());

    for (BalancesDTO dto : balanceEntity.getBalances()) {
      if (dto.getCategoryName().contains(categoryName)) {
        dto.setAmount(amount);
        dto.setBalance(amount - (beforeAmount - dto.getBalance()));
      }
    }

    redisRepository.save(balanceEntity);
  }

  // 예산 삭제
  public void deleteBalance(Long userId, String categoryName) {
    BalanceEntity balanceEntity = redisRepository.findByUserId(userId.toString());

    balanceEntity.getBalances().removeIf(balance -> balance.getCategoryName().equals(categoryName));

    redisRepository.save(balanceEntity);
  }



  // 남은 금액 수정
  public void updateBalance(UserEntity user, Long amount, String categoryName) {
    BalanceEntity balanceEntity = redisRepository.findByUserId(user.getId().toString());

    for (BalancesDTO balance : balanceEntity.getBalances()) {
      if (balance.getCategoryName().contains(categoryName)) {
        balance.setBalance(calculateBalance(user, balance.getBalance(), amount, categoryName));
      }
    }

    redisRepository.save(balanceEntity);
  }

  // 남은 예산 금액 계산
  private long calculateBalance(UserEntity user, Long balance, Long amount, String categoryName) {
    long totalBalance = balance - amount;

    if (totalBalance < 0) {
      sendMessage(user, categoryName + " 예산을 초과 했습니다.");
    }

    return totalBalance;
  }

  // 경고 메시지 전송
  public void sendMessage(UserEntity user, String message) {
    WaringMessageEntity warningMessage = WaringMessageEntity.builder()
        .user(user)
        .message(message)
        .recivedDateTime(LocalDateTime.now())
        .build();

    waringMessageRepository.save(warningMessage);
    redisTemplate.convertAndSend("notifications", new MessageDTO(user.getId(), message));
  }
}

