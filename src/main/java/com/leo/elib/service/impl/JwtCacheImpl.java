package com.leo.elib.service.impl;

import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.service.inter.JwtCache;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class JwtCacheImpl implements JwtCache {
    
    private final int versionBegin = 0;
    
    @Value("${container.redis.at_hash}")
    private String atHashCont;
    @Value("${container.redis.rt_hash}")
    private String rtHashCont;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    private String getTokenHashKey(int userId,DeviceTypeEnum deviceType) {
        return userId + ":" + deviceType.getValue();
    }
    @Override
    public Integer getVersion(boolean tokenType, int userId, DeviceTypeEnum deviceType) {
        assert deviceType != null;
        var res = redisTemplate.opsForHash().get(
            tokenType ? atHashCont : rtHashCont, 
            getTokenHashKey(userId, deviceType)
        );
        return res == null ? null : (Integer) res;
    }

    @Override
    public Pair<Integer, Boolean> getVersionSetIfAbsent(boolean tokenType, int userId, DeviceTypeEnum deviceType) {
        assert deviceType != null;
        var key = getTokenHashKey(userId, deviceType);
        var res = redisTemplate.opsForHash().get(
            tokenType ? atHashCont : rtHashCont, 
            key
        );
        if (res == null) {
            redisTemplate.opsForHash().put(
                tokenType ? atHashCont : rtHashCont, 
                key, 
                versionBegin
            );
            return Pair.of(versionBegin, true);
        }
        return Pair.of((Integer) res, false);
    }
    

    @Override
    public Pair<Integer, Integer> incTokenVersionGetNew(int userId, DeviceTypeEnum deviceType) {
        assert deviceType != null;
        var key = getTokenHashKey(userId, deviceType);
        var newAtVer = redisTemplate.opsForHash().increment(atHashCont, key, 1);
        key = getTokenHashKey(userId, deviceType);
        var newRtVer = redisTemplate.opsForHash().increment(rtHashCont, key, 1);
        return Pair.of(newAtVer.intValue(), newRtVer.intValue());
    }
}
