package com.zero.pennywise.config.batch.step;

import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DeleteSavingDataStep {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final SavingsRepository savingsRepository;

  // savings 테이블에서 해당 저축 정보 삭제
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
