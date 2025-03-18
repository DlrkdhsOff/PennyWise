package com.zero.pennywise.domain.model.response.page;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<T> dataList;

  public static <T> PageResponse<T> of(List<T> list, int pageNumber) {
    int pageSize = 5;  // 페이지 크기 설정

    pageNumber = pageNumber > 0 ? pageNumber : 1;
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
    int start = (int) pageable.getOffset();

    // start 인덱스가 데이터 리스트 크기를 초과하면 빈 페이지 반환
    if (start >= list.size()) {
      return new PageResponse<>(pageNumber, 0, 0L, List.of());
    }

    int end = Math.min(start + pageSize, list.size());
    List<T> pagedList = list.subList(start, end);

    // 페이징 객체 생성
    Page<T> page = new PageImpl<>(pagedList, pageable, list.size());

    // Page를 PageResponse로 변환
    return new PageResponse<>(
        page.getNumber() + 1,   // 페이지는 1부터 시작하게 설정
        page.getTotalPages(),   // 총 페이지 수
        page.getTotalElements(),// 전체 데이터 개수
        page.getContent()       // 현재 페이지 데이터 리스트
    );
  }
}
