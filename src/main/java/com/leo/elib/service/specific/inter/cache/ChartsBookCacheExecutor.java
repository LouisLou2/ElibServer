package com.leo.elib.service.specific.inter.cache;

import com.leo.elib.entity.BookInfo;

import java.util.List;
import java.util.Set;

public interface ChartsBookCacheExecutor {
  void deleteBatch(String[] isbns);
  Set<String> getCachedBooksIsbnClone();
  void storeBook(List<BookInfo> books);
  List<BookInfo> getBooksWithoutLibs(List<String> isbns);
  BookInfo getBookWithoutLibs(String isbn);
}