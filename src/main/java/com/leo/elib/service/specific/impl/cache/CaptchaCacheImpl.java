package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.comp_struct.CacheWithTime;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.CaptchaCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class CaptchaCacheImpl implements CaptchaCache {

  @Value("${container.redis.authen-code.authen-code-hash}")
  private String authenCodeCont;
  @Value("${container.redis.authen-code.authen-code-zset}")
  private String authenCodeZset;
  @Value("${authen.setting.expiration}") //seconds
  private int authenCodeExpiration;

  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> hashOps;
  private ZSetOperations<String, String> zsetOps;

  @PostConstruct
  private void init() {
    hashOps = rCacheManager.getOpsForHash();
    zsetOps = rCacheManager.getOpsForZSet();
  }

  private String getCaptchaCodeKey(String ip, long timestamp) {
    return ip + ":" + timestamp;
  }
  @Override
  public String storeCaptchaCode(String ip, long timestamp, String code) {
    assert code != null;
    String key = getCaptchaCodeKey(ip, timestamp);
    var cache = new CacheWithTime(code, System.currentTimeMillis() + authenCodeExpiration * 1000L);
    hashOps.put(authenCodeCont, key, cache);
    zsetOps.add(authenCodeZset, key, cache.getExpireAt());
    return key;
  }

  @Override
  public boolean verifyCaptchaCode(String key, String code) {
    assert code != null;
    // 先尝试从hash中获取
    var cache = hashOps.get(authenCodeCont, key);
    // 如果为null，说明已经删除
    if (cache == null) {
      return false;
    }
    // 如果还存在，判断是否过期
    if (System.currentTimeMillis() > ((CacheWithTime) cache).getExpireAt()) {
      // 过期,不去管，等待定时任务删除
      return false;
    }else{
      return code.equals(((CacheWithTime) cache).getValue());
    }
  }
}