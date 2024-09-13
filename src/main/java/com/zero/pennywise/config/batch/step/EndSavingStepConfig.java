package com.zero.pennywise.config.batch.step;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDate;
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
public class EndSavingStepConfig {


  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final TransactionRepository transactionRepository;


  // 저축 만기시 거래 목록 type를 END로 변경
  @Bean
  public Step endSavingStep() {
    return new StepBuilder("endSavingStep", jobRepository)
        .<TransactionEntity, TransactionEntity>chunk(10, platformTransactionManager)
        .reader(endSavingReader())
        .processor(endSavingProcessor())
        .writer(endSavingWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<TransactionEntity> endSavingReader() {

    return new RepositoryItemReaderBuilder<TransactionEntity>()
        .name("endSavingReader")
        .repository(transactionRepository)
        .methodName("findByEndDate")
        .arguments(LocalDate.now())
        .pageSize(10)
        .sorts(Map.of("id", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<TransactionEntity, TransactionEntity> endSavingProcessor() {
    return transaction -> {
      transaction.setType(TransactionStatus.END);
      return transaction;
    };
  }

  @Bean
  public RepositoryItemWriter<TransactionEntity> endSavingWriter() {
    return new RepositoryItemWriterBuilder<TransactionEntity>()
        .repository(transactionRepository)
        .methodName("save")
        .build();
  }

}
