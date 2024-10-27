package com.leo.elib.usecase.inter;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfoForList;

import java.util.List;

public interface BookInfoProvider {
  BookInfoForList getBookInfo(String isbn);
  List<BookInfoForList> getBookInfoList(List<String> bookIsbns);
  List<BookInfoForList> getRecoBooks(int userId, int offset, int num);
  AuthorWithBookLis getAuthorWithBooks(int authorId, int num);
}
