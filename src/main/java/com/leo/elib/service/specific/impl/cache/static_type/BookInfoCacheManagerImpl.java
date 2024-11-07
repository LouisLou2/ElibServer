package com.leo.elib.service.specific.impl.cache.static_type;

import com.leo.elib.entity.BookInfo;
import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
import com.leo.elib.service.specific.inter.cache.static_type.BookInfoCacheManager;
import com.leo.elib.service.specific.inter.cache.static_type.LangCache;
import jakarta.annotation.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookInfoCacheManagerImpl implements BookInfoCacheManager {
  @Resource
  private BookCache bookCache;
  @Resource
  private LangCache langCache;

  @Override
  public void setBookInfoCachedFields(BookInfo bookInfo) {
    Pair<String,String> categoryNamePair = bookCache.getCategoryName(bookInfo.getCategory1(), bookInfo.getCategory2());
    List<String> tagNames = bookCache.getTagNames(bookInfo.getTagIds());
    bookInfo.setNames(
      langCache.getLangName((byte) bookInfo.getLangId()),
      categoryNamePair.getFirst(),
      categoryNamePair.getSecond(),
      tagNames
    );
  }
}