package com.zero.pennywise.config;

import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.model.response.transaction.EndSavindDTO;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
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
  private final SavingsRepository savingsRepository;

  @Bean
  public Job transactionJob() {
    return new JobBuilder("transactionJob", jobRepository)
        .start(fixedTransactionStep())
        .next(savingStartStep())
        .next(depositStep())
        .next(savingEndStep())
        .next(deleteSavingDataStep())
        .build();
  }

  // 고정 지출 등록
  @Bean
  public Step fixedTransactionStep() {
    return new StepBuilder("fixedTransactionStep", jobRepository)
        .<TransactionEntity, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(fixedTransactionReader())
        .processor(fixedTransactionProcessor())
        .writer(fixedTransactionWriter())
        .build();
  }

  // 오늘 날짜부터 1달 전인 00시 부터 23시 59분 59초에 해당 되는 모든 데이터 전달
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

  // 고정 지출/수입 데이터 입력
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

  // 고정 지출/수입 데이터 저장
  @Bean
  public RepositoryItemWriter<TransactionEntity> fixedTransactionWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }

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


  // 저축 정보중 오늘 날짜가 만기일인 경우 납입한 금액 전부 거래 목록에 저장
  @Bean
  public Step depositStep() {
    return new StepBuilder("depositStep", jobRepository)
        .<EndSavindDTO, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(depositReader())
        .processor(depositProcessor())
        .writer(depositWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<EndSavindDTO> depositReader() {

    return new RepositoryItemReaderBuilder<EndSavindDTO>()
        .name("depositReader")
        .repository(transactionRepository)
        .methodName("findAmountSumByType")
        .arguments(TransactionStatus.FIXED_EXPENSES, LocalDate.now())
        .pageSize(10)
        .sorts(Map.of("user", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<EndSavindDTO, TransactionEntity> depositProcessor() {
    return endSavindDTO -> TransactionEntity.builder()
        .user(endSavindDTO.getUser())
        .categoryId(endSavindDTO.getCategoryId())
        .type(TransactionStatus.INCOME)
        .amount(endSavindDTO.getSum())
        .description("저축 만기")
        .dateTime(LocalDateTime.now())
        .build();
  }

  @Bean
  public RepositoryItemWriter<TransactionEntity> depositWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }


  // 저축 만기시 거래 목록 type를 END로 변경
  @Bean
  public Step savingEndStep() {
    return new StepBuilder("savingEndStep", jobRepository)
        .<TransactionEntity, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(savingEndReader())
        .processor(savingEndProcessor())
        .writer(savingEndWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<TransactionEntity> savingEndReader() {

    return new RepositoryItemReaderBuilder<TransactionEntity>()
        .name("savingEndReader")
        .repository(transactionRepository)
        .methodName("findByEndDate")
        .arguments(LocalDate.now())
        .pageSize(10)
        .sorts(Map.of("id", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<TransactionEntity, TransactionEntity> savingEndProcessor() {
    return transaction -> {
      transaction.setType(TransactionStatus.END);
      return transaction;
    };
  }

  @Bean
  public RepositoryItemWriter<TransactionEntity> savingEndWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }


  @Bean
  public Step deleteSavingDataStep() {
    return new StepBuilder("deleteSavingDataStep", jobRepository)
        .<SavingsEntity, Long>chunk(10, platformTransactionManager)
        .reader(deleteSavingDataReader())
        .processor(deleteSavingDataProcessor())
        .writer(deleteSavingDataWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<SavingsEntity> deleteSavingDataReader() {
    return new RepositoryItemReaderBuilder<SavingsEntity>()
        .name("deleteSavingDataReader")
        .repository(savingsRepository)
        .methodName("findByEndDate")
        .arguments(LocalDate.now())
        .pageSize(10)
        .sorts(Map.of("id", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<SavingsEntity, Long> deleteSavingDataProcessor() {
    return SavingsEntity::getId;
  }

  @Bean
  public ItemWriter<Long> deleteSavingDataWriter() {
    return id -> id.forEach(savingsRepository::deleteById);
  }
}


