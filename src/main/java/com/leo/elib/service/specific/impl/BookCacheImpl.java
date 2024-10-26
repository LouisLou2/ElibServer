package com.leo.elib.service.specific.impl;

import com.leo.elib.service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.BookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCacheImpl implements BookCache {

  @Value("${container.redis.book_category_hash}")
  private String bookCateHashCont;
  @Value("${container.redis.book_tag_hash_name}")
  private String bookTagNameHashCont;
  @Value("${container.redis.book_tag_hash_whole}")
  private String bookTagWholeHashCont;
  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> opsForHash;

  @PostConstruct
  void init () {
    opsForHash = rCacheManager.getOpsForHash();
  }
  @Override
  public Pair<String, String> getCategoryName(int categoryId, int subCategoryId) {
    var cateRes = opsForHash.get(bookCateHashCont, String.valueOf(categoryId));
    var subCateRes = opsForHash.get(bookCateHashCont, String.valueOf(subCategoryId));
    assert cateRes != null;
    assert subCateRes != null;
    return Pair.of((String) cateRes, (String) subCateRes);
  }

  @Override
  public List<String> getTagNames(List<Short> tagIds) {
    List<String> tagIdStrList = tagIds.stream()
                                      .map(String::valueOf)
                                      .collect(Collectors.toList());
    return opsForHash.multiGet(bookTagNameHashCont, tagIdStrList)
                     .stream()
                     .map(Object::toString)
                     .collect(Collectors.toList());
  }
}