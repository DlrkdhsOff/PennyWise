package com.zero.pennywise.component.scheduler;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

  private final JobLauncher jobLauncher;
  private final Job transactionJob;
  private final Logger logger = LoggerFactory.getLogger(BatchScheduler.class);

  @Scheduled(cron = "0 0 0 * * ?")
//@Scheduled(cron = "0/5 * * * * ?")
  public void runBatchJob() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    jobLauncher.run(transactionJob, jobParameters);
    logger.info("{} : transactionJob", LocalDate.now());
  }
}
