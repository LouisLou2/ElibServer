package com.leo.elib.service.specific.inter.cache;

import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.util.Pair;

import java.util.List;

public interface AuthorCache {
  Author getAuthorById(int authorId);

  /**
   * 获取作者的代表图书
   * @param authorId
   * @param num 获取的数量，必须>0,并且缓存的数量有限，只能获取有限的数量
   * @return 不管缓存中是否有这个作者的信息，只会返回缓存中的信息
   */
  @NotNull List<BookBrief> getBookBriefsByAuthorIdLazy(int authorId, int num, int offset);

  /**
   * 获取作者的代表图书
   * @param authorId
   * @param num 获取的数量，必须>0,并且缓存的数量有限，只能获取有限的数量
   * @return Boolean 指的是缓存中是否有这个作者的信息
   */
  @NotNull Pair<Boolean,List<BookBrief>> getBookBriefsByAuthorId(int authorId, int num, int offset);

  /**
   * 获取作者的代表图书
   * @param authorId
   * @param booksNum 获取的数量，必须>0,并且缓存的数量有限，只能获取有限的数量
   * @return
   */
  AuthorWithBookLis getAuthorWithBookList(int authorId, int booksNum);

  void insertAuthorWithBookLis(AuthorWithBookLis authorWithBookLis);

  int numOfBooksCachedPerAuthor();
}
