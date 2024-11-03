package com.leo.elib.service.specific.impl;

import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.service.specific.inter.JwtCache;
import com.leo.elib.service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class JwtCacheImpl implements JwtCache {
    
    private final int versionBegin = 0;
    
    @Value("${container.redis.token.at-hash}")
    private String atHashCont;
    @Value("${container.redis.token.rt-hash}")
    private String rtHashCont;
    @Resource
    private RCacheManager rCacheManager;
    private HashOperations<String, String, Object> opsForHash;
    
    @PostConstruct
    private void init () {
        opsForHash = rCacheManager.getOpsForHash();
    }
    
    
    private String getTokenHashKey(int userId,DeviceTypeEnum deviceType) {
        return userId + ":" + deviceType.getValue();
    }


    @Override
    public Integer getVersion(boolean tokenType, int userId, DeviceTypeEnum deviceType) {
        assert deviceType != null;
        var res = opsForHash.get(
            tokenType ? atHashCont : rtHashCont, 
            getTokenHashKey(userId, deviceType)
        );
        return res == null ? null : (Integer) res;
    }

    @Override
    public Pair<Integer, Boolean> getVersionSetIfAbsent(boolean tokenType, int userId, DeviceTypeEnum deviceType) {
        assert deviceType != null;
        var key = getTokenHashKey(userId, deviceType);
        var res = opsForHash.get(
            tokenType ? atHashCont : rtHashCont, 
            key
        );
        if (res == null) {
            opsForHash.put(
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
        var newAtVer = opsForHash.increment(atHashCont, key, 1);
        var newRtVer = opsForHash.increment(rtHashCont, key, 1);
        return Pair.of(newAtVer.intValue(), newRtVer.intValue());
    }
}
