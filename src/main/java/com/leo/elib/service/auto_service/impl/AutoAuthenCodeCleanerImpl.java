package com.leo.elib.service.auto_service.impl;

import com.leo.elib.service.auto_service.inter.AutoAnthenCodeCleaner;
import com.leo.elib.service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutoAuthenCodeCleanerImpl implements AutoAnthenCodeCleaner {

  @Value("${container.redis.authen-code.authen-code-hash}")
  private String authenCodeCont;
  @Value("${container.redis.authen-code.authen-code-zset}")
  private String authenCodeZset;

  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> hashOps;
  private ZSetOperations<String, String> zsetOps;

  @PostConstruct
  private void init() {
    hashOps = rCacheManager.getOpsForHash();
    zsetOps = rCacheManager.getOpsForZSet();
  }

  @Scheduled(initialDelayString = "${container.redis.authen-code.clean-interval-ms}", fixedDelayString = "${container.redis.authen-code.clean-interval-ms}")
  @Async("taskExecutor")
  @Override
  public void cleanAuthenCode() {
    long now = System.currentTimeMillis();
    var keys = zsetOps.rangeByScore(authenCodeZset, 0, now);
    if (keys != null) {
      keys.forEach(key -> {
        hashOps.delete(authenCodeCont, key);
        zsetOps.remove(authenCodeZset, key);
      });
    }
  }
}
