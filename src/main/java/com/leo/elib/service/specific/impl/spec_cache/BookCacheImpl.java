package com.leo.elib.service.specific.impl.spec_cache;

import com.leo.elib.service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.spec_cache.BookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookCacheImpl implements BookCache {

  @Value("${container.redis.book-cache.book-cate-hash}")
  private String bookCateHashCont;
  @Value("${container.redis.book-cache.book-tag-hash-name}")
  private String bookTagNameHashCont;
  @Value("${container.redis.book-cache.book-tag-hash-whole}")
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
    var cateRes = opsForHash.multiGet(bookCateHashCont, List.of(String.valueOf(categoryId), String.valueOf(subCategoryId)));
    assert cateRes.size() == 2;
    return Pair.of(cateRes.get(0).toString(), cateRes.get(1).toString());
  }

  @Override
  public String getCategoryName(int categoryId) {
    return Objects.requireNonNull(
      opsForHash.get(
        bookCateHashCont, 
        String.valueOf(categoryId)
      )
    ).toString();
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