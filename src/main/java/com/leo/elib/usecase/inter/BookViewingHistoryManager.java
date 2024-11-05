package com.leo.elib.usecase.inter;

import com.leo.elib.entity.BookViewingHistory;

import java.util.List;

public interface BookViewingHistoryManager {
  void addViewingHistory(BookViewingHistory history);
  List<BookViewingHistory> getViewingHistory(int userId, int pageNum, int pageSize);
  List<BookViewingHistory> getViewingHistory(int userId, String keyword, int pageNum, int pageSize);
}
