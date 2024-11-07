package com.leo.elib.service.specific.impl.cache;

import com.leo.elib.comp_struct.CacheWithTime;
import com.leo.elib.constant.AuthenCodeType;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.cache.VerifyCodeCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class VerifyCodeCacheImpl implements VerifyCodeCache {
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

  private String getVerifyCodeKey(String contact, AuthenCodeType type) {
    return contact + ":" + type.getValue();
  }

  @Override
  public void storeCode(String contact, AuthenCodeType type, String code) {
    assert code != null;
    CacheWithTime cache = new CacheWithTime(code, System.currentTimeMillis() + authenCodeExpiration* 1000L);
    hashOps.put(authenCodeCont, getVerifyCodeKey(contact, type), cache);
    zsetOps.add(authenCodeZset, getVerifyCodeKey(contact, type), cache.getExpireAt());
  }

  @Override
  public boolean verifyCode(String contact, AuthenCodeType type, String code) {
    assert code != null;
    // 先尝试从hash中获取
    var cache = hashOps.get(authenCodeCont, getVerifyCodeKey(contact, type));
    // 如果为null，说明已经删除
    if (cache == null) {
      return false;
    }
    // 如果还存在，判断是否过期
    // 下面可以类型强转的前提是，RedisConfig做好类型反序列化配置
    if (System.currentTimeMillis() > ((CacheWithTime) cache).getExpireAt()) {
      // 过期,不去管，等待定时任务删除
      return false;
    }else {
      return code.equals(((CacheWithTime) cache).getValue());
    }
  }
}