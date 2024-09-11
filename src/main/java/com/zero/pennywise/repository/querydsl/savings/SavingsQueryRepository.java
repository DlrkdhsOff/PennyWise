package com.zero.pennywise.repository.querydsl.savings;

import com.zero.pennywise.model.response.savings.SavingsDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsQueryRepository {

  Page<SavingsDataDTO> getAllSavings(Long id, Pageable page, Long categoryId);
}
