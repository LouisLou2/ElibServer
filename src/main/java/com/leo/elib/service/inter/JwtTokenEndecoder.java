package com.leo.elib.service.inter;

import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.constant.DeviceTypeEnum;

public interface JwtTokenEndecoder {
    /*
    * Create a token
    * @param tokenType true for access token, false for refresh token
    * @param userId
    * @param deviceType
    * @param version
    * @return String
    */
    String createToken(boolean tokenType, int userId, DeviceTypeEnum deviceType,int version);

    // if the verification is failed, return null
    TokenInfo parseToken(boolean tokenType, String token);
}
