package com.leo.elib.service.impl;


import com.leo.elib.service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class RCacheManagerImpl implements RCacheManager {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  
  private HashOperations<String, String, Object> opsForHash;
  private HashOperations<String, Integer, Object> opsForHashIntKey;
  private ZSetOperations<String, String> opsForZSet;

  @PostConstruct
  void init() {
    opsForHash = redisTemplate.opsForHash();
    opsForHashIntKey = redisTemplate.opsForHash();
    opsForZSet = redisTemplate.opsForZSet();
  }

  @Override
  public HashOperations<String, String, Object> getOpsForHash() {
    return opsForHash;
  }

  @Override
  public HashOperations<String, Integer, Object> getOpsForHashIntKey() {
    return opsForHashIntKey;
  }

  @Override
  public ZSetOperations<String, String> getOpsForZSet() {
    return opsForZSet;
  }
}
