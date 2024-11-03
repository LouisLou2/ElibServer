package com.leo.elib.usecase.inter;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.BookBrief;

import java.util.List;

public interface BookInfoProvider {
  BookInfo getBookInfo(String isbn);
  List<BookBrief> getRecoBooks(int userId, int offset, int num);
  AuthorWithBookLis getAuthorWithBooks(int authorId, int num);
  List<BookBrief> getBooksByAuthor(int authorId, int offset, int num);
}
