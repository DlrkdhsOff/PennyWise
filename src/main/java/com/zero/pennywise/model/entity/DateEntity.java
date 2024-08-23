package com.zero.pennywise.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class DateEntity {

  @CreatedDate
  @Column(name = "createAt", updatable = false)
  private LocalDate createAt;

  @LastModifiedDate
  private LocalDate updateAt;
}
