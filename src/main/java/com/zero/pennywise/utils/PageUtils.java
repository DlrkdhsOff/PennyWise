package com.zero.pennywise.utils;

public class PageUtils {

  public static long[] calculateOffset(String page, long pageSize, Long totalCount) {

    long[] data = new long[2];

    // 총 페이지 개수
    data[0] = (long) Math.ceil((double) totalCount / pageSize);
    long currentPage = Long.parseLong(page);

    if (currentPage > data[0]) {
      data[1] = -1;
      return data;
    }

    data[1] = (currentPage - 1) * pageSize;
    return data;
  }
}
