package com.leo.elib.service.impl;


import com.leo.elib.service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RCacheManagerImpl implements RCacheManager {
  @Resource
  private RedisTemplate<String, Object> redisTemplate;
  
  private HashOperations<String, String, Object> opsForHash;

  @PostConstruct
  void init() {
    opsForHash = redisTemplate.opsForHash();
  }

  @Override
  public HashOperations<String, String, Object> getOpsForHash() {
    return opsForHash;
  }
}
