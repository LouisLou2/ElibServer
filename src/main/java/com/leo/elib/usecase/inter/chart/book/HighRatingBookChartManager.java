package com.leo.elib.usecase.inter.chart.book;

import com.leo.elib.entity.dto.dao.BookBrief;

import java.util.List;
import java.util.Set;

public interface HighRatingBookChartManager {
  void updateHighRatingBooks();
  Set<String> highRatingBookIsbn();
  Set<String> highRatingBookIsbnClone();
  // pageNum从0开始
  List<BookBrief> getHighRatingBooks(int pageNum, int pageSize);
}
