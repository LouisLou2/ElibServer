package com.leo.elib.usecase.impl;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.comp_struct.TokenPair;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.TokenRes;
import com.leo.elib.service.specific.inter.cache.JwtCache;
import com.leo.elib.usecase.inter.AuthTokenManager;
import com.leo.elib.service.base_service.inter.JwtTokenEndecoder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenManagerImpl implements AuthTokenManager {
    
    @Resource
    private JwtTokenEndecoder jwtTokenEndecoder;
    
    @Resource
    private JwtCache jwtCache;
    
    @Override
    public Expected<TokenInfo, TokenRes> verifyToken(boolean tokenType, String token) {
        var tokenInfo = jwtTokenEndecoder.parseToken(tokenType,token);
        if (tokenInfo == null)
            return Expected.error(TokenRes.VerifyFailed);
        var version = jwtCache.getVersion(tokenType, tokenInfo.getUserId(), tokenInfo.getDeviceType());
        // 这里几乎不可能有null的情况，因为createAT一定会设置version
        assert version != null; // 这个在debug阶段先用着
        if (version != tokenInfo.getVersion()) {
            return Expected.error(TokenRes.TokenNotInUse);
        }
        return Expected.success(tokenInfo);
    }

    /*
    * Create an access token
    * @param tokenReq
    * @return String
    * @note 对于AT的创建，version字段会自动从redisCache中获取
    */
    @Override
    public String createAT(int userId, DeviceTypeEnum deviceType) {
        // 先查找缓存中是的version
        var res = jwtCache.getVersionSetIfAbsent(true, userId, deviceType);
        // 对于AT的设置，如果缓存中没有version,自然新插入，如果原来有，也不会变动version
        return jwtTokenEndecoder.createToken(true, userId, deviceType, res.getFirst());
    }

    @Override
    public TokenPair refreshTokenPair(int userId, DeviceTypeEnum deviceType) {
        var tokenVersions = jwtCache.incTokenVersionGetNew(userId, deviceType);
        return new TokenPair(
            jwtTokenEndecoder.createToken(true, userId, deviceType, tokenVersions.getFirst()),
            jwtTokenEndecoder.createToken(false, userId, deviceType, tokenVersions.getSecond())
        );
    }

}