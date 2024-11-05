package com.leo.elib.service.specific.impl.spec_cache;

import com.leo.elib.entity.BookInfo;
import com.leo.elib.service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.spec_cache.PopularBookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularBookCacheImpl implements PopularBookCache {

  @Value("${container.redis.popular_cache.book}")
  private String cacheCont;

  @Resource
  private RCacheManager rCacheManager;

  private HashOperations<String, String, Object> opsForHash;

  @PostConstruct
  void init () {
    opsForHash = rCacheManager.getOpsForHash();
  }

  @Override
  public void cacheBook(BookInfo info) {
    opsForHash.put(cacheCont, info.getIsbn(), info);
  }

  @Override
  public BookInfo getBook(String isbn) {
    var res = opsForHash.get(cacheCont, isbn);
    return res == null ? null : (BookInfo) res;
  }

  @Override
  public List<BookInfo> getBooks(List<String> isbns) {
    var res = opsForHash.multiGet(cacheCont, isbns);
    return (List<BookInfo>) (List<?>) res;
  }
}