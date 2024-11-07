package com.leo.elib.service.specific.impl.cache.static_type;

import com.leo.elib.entity.BookTag;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  // 应用内缓存
  Map<Integer,String> cateMap;
  Map<Short,String> tagNameMap;
  Map<Short, BookTag> tagMap;

  @PostConstruct
  void init () {
    opsForHash = rCacheManager.getOpsForHash();
    getCacheFromRedis();
  }

  private void getCacheFromRedis() {
    // 使用for循环的写法
    Map<String,Object> cateEntryMap = opsForHash.entries(bookCateHashCont);
    cateMap = new HashMap<>();
    for (var entry : cateEntryMap.entrySet()) {
      cateMap.put(Integer.parseInt(entry.getKey()), Objects.requireNonNull(entry.getValue()).toString());
    }
    // 使用stream的写法
    tagNameMap = opsForHash.entries(bookTagNameHashCont)
                      .entrySet()
                      .stream()
                      .collect(Collectors.toMap(
                        e -> Short.parseShort(e.getKey()),
                        e -> Objects.requireNonNull(e.getValue()).toString()
                      ));
    var it = opsForHash.get(bookTagWholeHashCont, "22");
    tagMap = opsForHash.entries(bookTagWholeHashCont)
                      .entrySet()
                      .stream()
                      .collect(Collectors.toMap(
                        e -> Short.parseShort(e.getKey()),
                        e -> (BookTag) e.getValue()
                      ));
  }


  @Override
  public Pair<String, String> getCategoryName(int categoryId, int subCategoryId) {
    return Pair.of(
      cateMap.get(categoryId),
      cateMap.get(subCategoryId)
    );
  }

  @Override
  public String getCategoryName(int categoryId) {
    return cateMap.get(categoryId);
  }

  @Override
  public List<String> getTagNames(List<Short> tagIds) {
    return tagIds.stream()
      .map(tagNameMap::get)
      .collect(Collectors.toList());
  }
}