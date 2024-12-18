package com.leo.elib.usecase.inter;


import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.BookBrief;

import java.util.List;

public interface AuthorInfoProvider {
  AuthorWithBookLis getAuthorWithBooks(int authorId, int num);
  List<BookBrief> getBooksByAuthor(int authorId, int offset, int num);
}
