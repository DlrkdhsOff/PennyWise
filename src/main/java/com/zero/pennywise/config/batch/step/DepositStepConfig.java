package com.zero.pennywise.config.batch.step;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.model.response.transaction.EndSavindDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.enums.TransactionStatus;
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
public class DepositStepConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final TransactionRepository transactionRepository;


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

}
