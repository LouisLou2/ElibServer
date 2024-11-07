//package com.leo.elib.service.specific.impl.cache;
//
//import com.leo.elib.entity.dto.dao.Author;
//import com.leo.elib.service.base_service.inter.RCacheManager;
//import com.leo.elib.service.specific.inter.cache.PopularAuthorCache;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.Resource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PopularAuthorCacheImpl implements PopularAuthorCache {
//
//  @Value("${container.redis.popular_cache.author}")
//  private String cacheCont;
//
//  @Resource
//  private RCacheManager rCacheManager;
//
//  private HashOperations<String, Integer, Object> opsForHash;
//
//  @PostConstruct
//  void init () {
//    opsForHash = rCacheManager.getOpsForHashIntKey();
//  }
//
//  @Override
//  public void cacheAuthor(Author author) {
//    opsForHash.put(cacheCont, author.getAuthorId(), author);
//  }
//
//  @Override
//  public Author getAuthor(int authorId) {
//    var res = opsForHash.get(cacheCont, authorId);
//    return res == null ? null : (Author) res;
//  }
//
//  @Override
//  public List<Author> getAuthors(List<Integer> authorIds) {
//    var res = opsForHash.multiGet(cacheCont, authorIds);
//    return (List<Author>) (List<?>) res;
//  }
//}