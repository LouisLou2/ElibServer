package com.leo.elib;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.specific.inter.cache.AuthorCacheNew;
import com.leo.elib.usecase.inter.BookInfoProvider;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AuthorInfoTest {
  @Resource
  private AuthorCacheNew authorCacheNew;

  @Resource
  private BookInfoMapper bookInfoMapper;

  @Test
  void testGetAuthorAndBooksByAuthorId() {
    NullablePair<Boolean, List<BookBrief>> books = authorCacheNew.getBookBriefsByAuthorId(5779, 10, 0);
    NullablePair<Boolean, AuthorWithBookLis> authorWithBooks = authorCacheNew.getAuthorAndBooksByAuthorId(5779, 10);
    AuthorWithBookLis awbl = bookInfoMapper.getAuthorWithBooks(5779, 10);
    authorCacheNew.insertAuthorWithBookLis(awbl);
    int x =0;
  }
}
