package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.NullablePair;
import com.leo.elib.comp_struct.RespWrapper;
import com.leo.elib.entity.AuthorWithBookLis;
import com.leo.elib.entity.dto.dao.BookBrief;
import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.specific.inter.cache.AuthorCacheNew;
import com.leo.elib.usecase.inter.AuthorInfoProvider;
import com.leo.elib.util.ListUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorInfoProviderImpl implements AuthorInfoProvider {
  @Resource
  private BookInfoMapper bookInfoMapper;

  @Resource
  private AuthorCacheNew authorCache;

  @Override
  public AuthorWithBookLis getAuthorWithBooks(int authorId, int num) {
    NullablePair<Boolean, AuthorWithBookLis> cacheRes = authorCache.getAuthorAndBooksByAuthorId(authorId, num);
    if (cacheRes.getFirst()) {
      return cacheRes.getSecond();
    }
    // 选择加入缓存
    AuthorWithBookLis authorWithBookList = bookInfoMapper.getAuthorWithBooks(authorId, Integer.MAX_VALUE);
    if (authorWithBookList == null) {
      return null;
    }
    authorCache.insertAuthorWithBookLis(authorWithBookList);
    List<BookBrief> books = new ArrayList<>(ListUtil.safeSubList(0, num, authorWithBookList.getBooks()));
    return authorWithBookList.cloneWithNewBooks(books);
  }

  @Override
  public List<BookBrief> getBooksByAuthor(int authorId, int offset, int num) {
    NullablePair<Boolean, List<BookBrief>> cacheRes = authorCache.getBookBriefsByAuthorId(authorId, num, offset);
    if (cacheRes.getFirst()) {
      return cacheRes.getSecond();
    }
    AuthorWithBookLis authorWithBookList = bookInfoMapper.getAuthorWithBooks(authorId, Integer.MAX_VALUE);
    if (authorWithBookList == null) {
      return null;
    }
    authorCache.insertAuthorWithBookLis(authorWithBookList);
    return ListUtil.safeSubList(offset, num, authorWithBookList.getBooks());
  }
}
