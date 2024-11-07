package com.leo.elib.service.base_service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.leo.elib.comp_struct.TokenInfo;
import com.leo.elib.constant.DeviceTypeEnum;
import com.leo.elib.service.base_service.inter.JwtTokenEndecoder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtTokenEndecoderImpl implements JwtTokenEndecoder {
    
    @Value("${jwt.at_expiration}")
    private int ATExpiresIn;
    @Value("${jwt.rt_expiration}")
    private int RTExpiresIn;
    @Value("${jwt.at_secret}")
    private String atSecret;
    @Value("${jwt.rt_secret}")
    private String rtSecret;
    
    private JWTVerifier atVerifier;
    private JWTVerifier rtVerifier;
    
    @PostConstruct
    public void init() {
        atVerifier = JWT.require(Algorithm.HMAC256(atSecret)).build();
        rtVerifier = JWT.require(Algorithm.HMAC256(rtSecret)).build();
    }
    

    @Override
    public String createToken(boolean tokenType, int userId, DeviceTypeEnum deviceType, int version) {
        Instant now = Instant.now();
        return JWT.create()
            .withClaim("userId", userId)
            .withClaim("deviceType", deviceType.getValue())
            .withClaim("version", version)
            .withIssuedAt(now)
            .withExpiresAt(
                now.plusSeconds(
                  tokenType ? ATExpiresIn : RTExpiresIn
                )
            )
            .sign(
              Algorithm.HMAC256(
                tokenType ? atSecret : rtSecret
              )
            );
    }

    public TokenInfo parseToken(boolean tokenType, String token) {
        try {
            var decodedJWT = (tokenType ? atVerifier : rtVerifier).verify(token);
            var deviceTypeEnum = DeviceTypeEnum.valueOf(decodedJWT.getClaim("deviceType").asInt());
            return new TokenInfo(
                decodedJWT.getClaim("userId").asInt(),
                deviceTypeEnum,
                decodedJWT.getClaim("version").asInt()
            );
        } catch (Exception e) {
            return null;
        }
    }
}
 