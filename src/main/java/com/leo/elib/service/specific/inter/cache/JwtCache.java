package com.leo.elib.service.specific.inter.cache;


import com.leo.elib.constant.DeviceTypeEnum;
import org.springframework.data.util.Pair;

public interface JwtCache {
    /*
    * @param tokenType: true for access token, false for refresh token
    * @param userId: user id
    * @param deviceType: device type
        * @return version of token, null if not found，但是照逻辑应该不会返回null，因为一定会有一个版本号
    * */
    Integer getVersion(boolean tokenType, int userId, DeviceTypeEnum deviceType);
    /*
    * @param tokenType: true for access token, false for refresh token
    * @note: 给出该组合的缓存中的版本号，如果不存在，就在缓存中设置一个并返回设置好的值
    */
    Pair<Integer, Boolean> getVersionSetIfAbsent(boolean tokenType, int userId, DeviceTypeEnum deviceType);
    
    Pair<Integer,Integer> incTokenVersionGetNew(int userId, DeviceTypeEnum deviceType);
}
