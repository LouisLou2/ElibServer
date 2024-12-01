package com.leo.elib.service.specific.inter.cache;

import com.leo.elib.entity.BookInfo;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Set;

public interface ChartsBookCacheExecutor {
  void deleteBatch(String[] isbns);
  Set<String> getCachedBooksIsbnClone();
  void storeBook(List<BookInfo> books);

  // 第二个参数，true表示所有数据均不在缓存中， false表示有部分数据或者全部在缓存中
  Pair<List<BookInfo>,Boolean> getBooksWithoutLibs(List<String> isbns);
  BookInfo getBookWithoutLibs(String isbn);
}