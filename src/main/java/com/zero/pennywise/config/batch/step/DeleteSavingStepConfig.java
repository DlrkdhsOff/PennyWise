package com.zero.pennywise.config.batch.step;

import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.repository.SavingsRepository;
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
public class DeleteSavingStepConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final SavingsRepository savingsRepository;

  // savings 테이블에서 해당 저축 정보 삭제
  @Bean
  public Step deleteSavingStep() {
    return new StepBuilder("deleteSavingStep", jobRepository)
        .<SavingsEntity, Long>chunk(10, platformTransactionManager)
        .reader(deleteSavingReader())
        .processor(deleteSavingProcessor())
        .writer(deleteSavingWriter())
        .build();
  }

  @Bean
  public RepositoryItemReader<SavingsEntity> deleteSavingReader() {
    return new RepositoryItemReaderBuilder<SavingsEntity>()
        .name("deleteSavingReader")
        .repository(savingsRepository)
        .methodName("findByEndDate")
        .arguments(LocalDate.now())
        .pageSize(10)
        .sorts(Map.of("id", Sort.Direction.ASC))
        .build();
  }

  @Bean
  public ItemProcessor<SavingsEntity, Long> deleteSavingProcessor() {
    return SavingsEntity::getId;
  }

  @Bean
  public ItemWriter<Long> deleteSavingWriter() {
    return id -> id.forEach(savingsRepository::deleteById);
  }

}
