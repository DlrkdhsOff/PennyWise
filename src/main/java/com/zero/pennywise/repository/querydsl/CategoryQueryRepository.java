package com.zero.pennywise.repository.querydsl;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryQueryRepository {

  List<String> getAllCategory(Long userId, String page);

}