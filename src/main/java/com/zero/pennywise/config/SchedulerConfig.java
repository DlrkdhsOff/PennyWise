package com.zero.pennywise.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {

  @Bean
  public ThreadPoolTaskScheduler multiThreadScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(5);
    return scheduler;
  }

  @Bean
  public ThreadPoolTaskScheduler singleThreadScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(1);
    return scheduler;
  }
}
