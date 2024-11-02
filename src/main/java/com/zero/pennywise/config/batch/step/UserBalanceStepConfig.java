//package com.zero.pennywise.config.batch.step;
//
//import com.zero.pennywise.component.handler.CategoryHandler;
//import com.zero.pennywise.entity.BudgetEntity;
//import com.zero.pennywise.entity.CategoryEntity;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.entity.redis.BalanceEntity;
//import com.zero.pennywise.model.request.budget.BalancesDTO;
//import com.zero.pennywise.repository.BudgetRepository;
//import com.zero.pennywise.repository.RedisRepository;
//import com.zero.pennywise.repository.UserRepository;
//import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.PlatformTransactionManager;
//
//
//@Configuration
//@RequiredArgsConstructor
//public class UserBalanceStepConfig {
//  private final JobRepository jobRepository;
//  private final PlatformTransactionManager platformTransactionManager;
//  private final UserRepository userRepository;
//  private final BudgetRepository budgetRepository;
//  private final TransactionQueryRepository transactionQueryRepository;
//  private final CategoryHandler categoryHandler;
//  private final RedisRepository redisRepository;
//
//  @Bean
//  public Step userBalanceStep() {
//    return new StepBuilder("userBalanceStep", jobRepository)
//        .<UserEntity, BalanceEntity>chunk(10, platformTransactionManager)
//        .reader(userItemReader())
//        .processor(userBudgetProcessor())
//        .writer(redisBalanceWriter())
//        .build();
//  }
//
//  @Bean
//  public ItemReader<UserEntity> userItemReader() {
//    return new RepositoryItemReaderBuilder<UserEntity>()
//        .name("userItemReader")
//        .repository(userRepository)
//        .methodName("findAll")
//        .sorts(Map.of("id", Sort.Direction.ASC))
//        .build();
//  }
//
//  @Bean
//  public ItemProcessor<UserEntity, BalanceEntity> userBudgetProcessor() {
//    return user -> {
//      List<BudgetEntity> userBudgets = budgetRepository.findAllByUserId(user.getId());
//      if (userBudgets == null) {
//        return null;
//      }
//
//      List<BalancesDTO> balances = new ArrayList<>();
//      for (BudgetEntity budget : userBudgets) {
//        balances.add(getCategoryBalances(user.getId(), budget));
//      }
//
//      return new BalanceEntity(user.getId().toString(), balances);
//    };
//  }
//
//  private BalancesDTO getCategoryBalances(Long userId, BudgetEntity budget) {
//    CategoryEntity category = categoryHandler
//        .getCateogryByUserIdAndId(userId, budget.getCategory().getCategoryId());
//
//    Long totalExpenses = transactionQueryRepository
//        .getExpenses(userId, category.getCategoryId(), LocalDate.now().toString());
//
//    Long amount = budget.getAmount();
//    totalExpenses = amount - totalExpenses;
//
//    return new BalancesDTO(category.getCategoryName(), amount, totalExpenses);
//  }
//
//  @Bean
//  public ItemWriter<BalanceEntity> redisBalanceWriter() {
//    return items -> {
//      for (BalanceEntity balances : items) {
//        if (balances != null) {
//          redisRepository.save(balances);
//        }
//      }
//    };
//  }
//}