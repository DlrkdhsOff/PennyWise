package com.zero.pennywise.config.batch.step;

import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class SavingStartStepConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final TransactionRepository transactionRepository;
  private final SavingsRepository savingsRepository;


  // 저축을 시작일과 일치하는 저축 정보의 금액을 거래에 등록
  @Bean
  public Step savingStartStep() {
    return new StepBuilder("savingStep", jobRepository)
        .<SavingsEntity, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(savingStartReader())
        .processor(savingStartProcessor())
        .writer(savingStartWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<SavingsEntity> savingStartReader() {

    return new RepositoryItemReaderBuilder<SavingsEntity>()
        .name("savingStartReader")
        .repository(savingsRepository)
        .methodName("findByStartDate")
        .arguments(LocalDate.now())
        .pageSize(10)
        .sorts(Map.of("id", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<SavingsEntity, TransactionEntity> savingStartProcessor() {
    return savings -> TransactionEntity.builder()
        .user(savings.getUser())
        .categoryId(savings.getCategory().getCategoryId())
        .type(TransactionStatus.FIXED_EXPENSES)
        .amount(savings.getAmount())
        .description(savings.getName() + savings.getDescription())
        .dateTime(LocalDateTime.now())
        .build();
  }

  @Bean
  public RepositoryItemWriter<TransactionEntity> savingStartWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }

}
