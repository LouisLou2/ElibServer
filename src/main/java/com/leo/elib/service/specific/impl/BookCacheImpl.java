package com.leo.elib.service.specific.impl;

import com.leo.elib.service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.BookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.util.Pair;

public class BookCacheImpl implements BookCache {
  @Value("${container.redis.book_category_hash}")
  private String bookCateHashCont;
  @Value("${container.redis.book_tag_hash}")
  private String bookTagHashCont;
  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> opsForHash;

  @PostConstruct
  void init () {
    opsForHash = rCacheManager.getOpsForHash();
  }
  private String getBookCateHashKey(byte level, int cateId){
    assert level == 1 || level == 2;
    return level + ":" + cateId;
  }
  @Override
  public Pair<String, String> getCategoryName(int categoryId, int subCategoryId) {
    var cateRes = opsForHash.get(bookCateHashCont, getBookCateHashKey((byte) 1, categoryId));
    var subCateRes = opsForHash.get(bookCateHashCont, getBookCateHashKey((byte) 2, subCategoryId));
    return Pair.of((String) cateRes, (String) subCateRes);
  }
}