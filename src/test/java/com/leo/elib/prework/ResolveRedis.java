package com.leo.elib.prework;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ResolveRedis {
  @Resource
  private RedisTemplate<String, Object> redisTemplate;
  @Value("${authen.setting.expiration}")
  private int expiration;
  @Value("${container.redis.authen-code.authen-code-hash}")
  private String authenHashCont;
  @Test
  void testEnum() {
//        var status = LibBookStatusEnum.AVAILABLE;
//        String it = "r"+status;
//        System.out.println(it);
//        long x = 1231;
//        it += x;
//        System.out.println(it);
    redisTemplate.expire(authenHashCont, expiration, TimeUnit.SECONDS);
  }
}
