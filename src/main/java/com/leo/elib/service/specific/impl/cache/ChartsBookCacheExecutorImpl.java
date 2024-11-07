package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.entity.BookInfo;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.ChartsBookCacheExecutor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChartsBookCacheExecutorImpl implements ChartsBookCacheExecutor {

  @Value("${container.redis.chart_cache.book.cont-name}")
  private String cacheCont;

  @Value("${container.redis.chart_cache.book.max_capacity}")
  private int maxCapacity;

  @Resource
  private RCacheManager rCacheManager;

  private HashOperations<String, String, Object> opsForHash;


  private Set<String> cachedBooksIsbn;

  @PostConstruct
  void init () {
    opsForHash = rCacheManager.getOpsForHash();
    cachedBooksIsbn = opsForHash.keys(cacheCont);
  }

  @Override
  public void deleteBatch(String[] isbns) {
    assert isbns!=null;
    if (isbns.length == 0) {
      return;
    }
    opsForHash.delete(cacheCont, isbns);
    cachedBooksIsbn.removeAll(Set.of(isbns));
  }

  @Override
  public Set<String> getCachedBooksIsbnClone() {
    return new HashSet<>(cachedBooksIsbn);
  }

  @Override
  public void storeBook(List<BookInfo> books) {
    // TODO: 没有进行容量控制，这个之后再去做
    Map<String, BookInfo> map = new HashMap<>();
    for (var book : books) {
      map.put(book.getIsbn(), book);
    }
    opsForHash.putAll(cacheCont, map);
    cachedBooksIsbn.addAll(map.keySet());
  }

  @Override
  public BookInfo getBookWithoutLibs(String isbn) {
    if (!cachedBooksIsbn.contains(isbn)) {
      return null;
    }
    var res = opsForHash.get(cacheCont, isbn);
    return res == null ? null : (BookInfo) res;
  }

  @Override
  public List<BookInfo> getBooksWithoutLibs(List<String> isbns) {
    // 如果都不在，就没必要去缓存中取了，这里返回全都是null的List
    boolean allNotIn = isbns.stream().noneMatch(cachedBooksIsbn::contains);
    if (allNotIn) {
      return Collections.nCopies(isbns.size(), null);
    }
    var res = opsForHash.multiGet(cacheCont, isbns);
    return (List<BookInfo>) (List<?>) res;
  }
}