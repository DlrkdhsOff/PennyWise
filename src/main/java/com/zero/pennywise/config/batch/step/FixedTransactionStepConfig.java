package com.zero.pennywise.config.batch.step;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FixedTransactionStepConfig {

  private final TransactionRepository transactionRepository;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;

  // 고정 지출/수입 정보 자동 등록
  @Bean
  public Step fixedTransactionStep() {
    return new StepBuilder("fixedTransactionStep", jobRepository)
        .<TransactionEntity, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(fixedTransactionReader())
        .processor(fixedTransactionProcessor())
        .writer(fixedTransactionWriter())
        .build();
  }


  @Bean
  public RepositoryItemReader<TransactionEntity> fixedTransactionReader() {
    YearMonth lastMonth = YearMonth.from(LocalDate.now().minusMonths(1));

    LocalDateTime startDay = lastMonth.atDay(1).atStartOfDay();
    LocalDateTime endDay = lastMonth.atEndOfMonth().atTime(23, 59, 59);

    return new RepositoryItemReaderBuilder<TransactionEntity>()
        .name("fixedTransactionReader")
        .repository(transactionRepository)
        .methodName("findByLastMonthTransaction")
        .arguments(startDay, endDay)
        .pageSize(10)
        .sorts(Map.of("transactionId", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<TransactionEntity, TransactionEntity> fixedTransactionProcessor() {
    return transaction -> TransactionEntity.builder()
        .user(transaction.getUser())
        .categoryId(transaction.getCategoryId())
        .type(transaction.getType())
        .amount(transaction.getAmount())
        .description(transaction.getDescription())
        .dateTime(LocalDateTime.now())
        .build();

  }

  @Bean
  public RepositoryItemWriter<TransactionEntity> fixedTransactionWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }

}
