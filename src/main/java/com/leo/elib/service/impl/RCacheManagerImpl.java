package com.leo.elib.service.impl;


import com.leo.elib.service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class RCacheManagerImpl implements RCacheManager {
  @Resource
  private RedisTemplate<String, String> redisTemplate;
  
  private HashOperations<String, String, Object> opsForHash;
  private ZSetOperations<String, String> opsForZSet;

  @PostConstruct
  void init() {
    opsForHash = redisTemplate.opsForHash();
    opsForZSet = redisTemplate.opsForZSet();
  }

  @Override
  public HashOperations<String, String, Object> getOpsForHash() {
    return opsForHash;
  }

  @Override
  public ZSetOperations<String, String> getOpsForZSet() {
    return opsForZSet;
  }
}
