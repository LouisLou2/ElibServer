package com.leo.elib.service.specific.impl.cache.static_type;

import com.leo.elib.entity.BookCate;
import com.leo.elib.entity.BookTag;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.static_type.BookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookCacheImpl implements BookCache {

  private static final String debug_originalCateCont = "elib:hash:book_cate";

  @Value("${container.redis.book-cache.book-cate-info-hash}")
  private String bookCateHashCont;
  @Value("${container.redis.book-cache.book-tag-hash-name}")
  private String bookTagNameHashCont;
  @Value("${container.redis.book-cache.book-tag-hash-whole}")
  private String bookTagWholeHashCont;

  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> opsForHash;

  // 应用内缓存
  Map<Integer, BookCate> cateMap;
  Map<Integer,List<BookCate>> subCateMap;
  Map<Short,String> tagNameMap;
  Map<Short, BookTag> tagMap;

  @PostConstruct
  void init () {
    opsForHash = rCacheManager.getOpsForHash();
    getCacheFromRedis();
    setCatesParentId();
  }

  private void getCacheFromRedis() {
    // 使用for循环的写法
    Map<String,Object> cateEntryMap = opsForHash.entries(bookCateHashCont);
    cateMap = new HashMap<>();
    for (var entry : cateEntryMap.entrySet()) {
      cateMap.put(Integer.parseInt(entry.getKey()), (BookCate) entry.getValue());
    }
    // 使用stream的写法
    tagNameMap = opsForHash.entries(bookTagNameHashCont)
                      .entrySet()
                      .stream()
                      .collect(Collectors.toMap(
                        e -> Short.parseShort(e.getKey()),
                        e -> Objects.requireNonNull(e.getValue()).toString()
                      ));
    tagMap = opsForHash.entries(bookTagWholeHashCont)
                      .entrySet()
                      .stream()
                      .collect(Collectors.toMap(
                        e -> Short.parseShort(e.getKey()),
                        e -> (BookTag) e.getValue()
                      ));
  }

  private void setCatesParentId() {
    subCateMap = new HashMap<>();
    int subId = 0;
    for (var entry : cateMap.entrySet()) {
      BookCate cate = entry.getValue();
      subId = cate.getCateId() % 1000;

      if (subId == 0) {
        cate.setParentId(0);
      } else {
        int parentId = cate.getCateId() - subId;
        // 如果一级分类不在map中，就加入
        if (!subCateMap.containsKey(parentId)) {
          subCateMap.put(parentId, new ArrayList<>());
        }
        cate.setParentId(parentId);
        subCateMap.get(parentId).add(cate);
      }
    }
  }


  @Override
  public Pair<String, String> getCategoryName(int categoryId, int subCategoryId) {
    return Pair.of(
      cateMap.get(categoryId).getCateName(),
      cateMap.get(subCategoryId).getCateName()
    );
  }

  @Override
  public String getCategoryName(int categoryId) {
    return cateMap.get(categoryId).getCateName();
  }

  @Override
  public List<String> getTagNames(List<Short> tagIds) {
    return tagIds.stream()
      .map(tagNameMap::get)
      .collect(Collectors.toList());
  }

  @Override
  public List<BookCate> getCate(List<Integer> cateIds) {
    return cateIds.stream()
      .map(cateMap::get)
      .collect(Collectors.toList());
  }

  @Override
  public List<BookCate> getAllCates() {
    return new ArrayList<>(cateMap.values());
  }

  @Override
  public List<BookCate> getSubCates(int parentId) {
    return subCateMap.get(parentId);
  }

  @Override
  public Map<Integer, String> debug_GetAllCategoryNames() {
    Map<String,Object> cateEntryMap = opsForHash.entries(debug_originalCateCont);
    Map<Integer,String> cateMap1 = new HashMap<>();
    for (var entry : cateEntryMap.entrySet()) {
      cateMap1.put(Integer.parseInt(entry.getKey()), (String) entry.getValue());
    }
    return cateMap1;
  }
}