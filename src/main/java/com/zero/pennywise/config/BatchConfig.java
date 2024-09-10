package com.zero.pennywise.config;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final TransactionRepository transactionRepository;

  @Bean
  public Job transactionJob() {
    return new JobBuilder("transactionJob", jobRepository)
        .start(transactionStep())
        .build();
  }

  @Bean
  public Step transactionStep() {
    return new StepBuilder("transactionStep", jobRepository)
        .<TransactionEntity, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(transactionReader())
        .processor(transactionProcessor())
        .writer(transactionWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<TransactionEntity> transactionReader() {
    LocalDate lastMonthsDate = LocalDate.now().minusMonths(1);
    LocalDateTime startDay = lastMonthsDate.atStartOfDay();
    LocalDateTime endDay = lastMonthsDate.atTime(23, 59, 59);

    return new RepositoryItemReaderBuilder<TransactionEntity>()
        .name("transactionReader")
        .repository(transactionRepository)
        .methodName("findByLastMonthTransaction")
        .arguments(startDay, endDay)
        .pageSize(10)
        .sorts(Map.of("transactionId", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<TransactionEntity, TransactionEntity> transactionProcessor() {
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
  public RepositoryItemWriter<TransactionEntity> transactionWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }
}

