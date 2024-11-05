package com.leo.elib.service.specific.inter.spec_cache;

import com.leo.elib.entity.BookInfo;

import java.util.List;

public interface PopularBookCache {
  void cacheBook(BookInfo info);
  BookInfo getBook(String isbn);
  List<BookInfo> getBooks(List<String> isbns);
}
