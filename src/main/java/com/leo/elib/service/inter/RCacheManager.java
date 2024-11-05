package com.leo.elib.service.inter;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;

public interface RCacheManager {
  HashOperations<String, String, Object> getOpsForHash();
  HashOperations<String, Integer, Object> getOpsForHashIntKey();
  ZSetOperations<String, String> getOpsForZSet();
}
