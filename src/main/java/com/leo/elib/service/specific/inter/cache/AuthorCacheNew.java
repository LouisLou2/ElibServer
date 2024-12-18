package com.leo.elib.service.specific.inter.cache;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.Author;
import com.leo.elib.entity.dto.dao.BookBrief;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.util.Pair;

import java.util.List;

public interface AuthorCacheNew {
  // Author getAuthorById(int authorId);

  @NotNull
  NullablePair<Boolean,List<BookBrief>> getBookBriefsByAuthorId(int authorId, int num, int offset);

  NullablePair<Boolean,AuthorWithBookLis> getAuthorAndBooksByAuthorId(int authorId, int num);
  void insertAuthorWithBookLis(AuthorWithBookLis authorWithBookLis);
}
