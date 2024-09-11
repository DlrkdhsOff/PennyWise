package com.zero.pennywise.config.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfig {

  private final JobRepository jobRepository;

  private final Step fixedTransactionStep;
  private final Step savingStartStep;
  private final Step depositStep;
  private final Step endSavingStep;
  private final Step deleteSavingDataStep;


  @Bean
  public Job transactionJob() {
    return new JobBuilder("transactionJob", jobRepository)
        .start(fixedTransactionStep)
        .next(savingStartStep)
        .next(depositStep)
        .next(endSavingStep)
        .next(deleteSavingDataStep)
        .build();
  }

}


