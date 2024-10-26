package com.leo.elib.usecase.inter;

import com.leo.elib.comp_struct.Expected;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.comp_struct.TokenPair;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.constant.TokenRes;

public interface AuthTokenManager {
    
    /**
     * Verify the token
     *
     * @param tokenType true for access token, false for refresh token
     * @param token
     * @return Expected<TokenInfo, TokenRes> 说明：TokenInfo是token的信息，TokenRes是token的验证结果（异常结果）
     */
    Expected<TokenInfo, TokenRes> verifyToken(boolean tokenType,String token);
    
    String createAT(int userId, DeviceTypeEnum deviceType);
    
    // String createRTAndRefresh(int userId, DeviceTypeEnum deviceType);
    
    TokenPair refreshTokenPair(int userId, DeviceTypeEnum deviceType);
}
