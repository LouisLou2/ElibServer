package com.leo.elib.service.inter;

import org.springframework.data.redis.core.HashOperations;

public interface RCacheManager {
  HashOperations<String, String, Object> getOpsForHash();
}
