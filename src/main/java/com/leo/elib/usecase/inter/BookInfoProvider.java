package com.leo.elib.usecase.inter;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.BookInfo;
import com.leo.elib.entity.dto.dao.BookBrief;

import java.util.List;

public interface BookInfoProvider {
  BookInfo getBookInfo(String isbn,int relatedBookNum);
  AuthorWithBookLis getAuthorWithBooks(int authorId, int num);
  List<BookBrief> getBooksByAuthor(int authorId, int offset, int num);
  List<BookBrief> getRecoBooks(int userId, int offset, int num);
  // 注意这个方法是为了调试用的，不要在其他地方调用，还有就是，因为有的书友几个作者，所以返回的数目不一定是num，这与调试目的无关，故没有修改
  List<BookInfo> debug_getBooksByIsbn(int offset, int num);
}
