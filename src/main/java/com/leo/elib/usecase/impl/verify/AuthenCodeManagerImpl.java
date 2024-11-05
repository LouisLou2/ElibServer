package com.leo.elib.usecase.impl.verify;

import com.leo.elib.constant.AuthenCodeType;
import com.leo.elib.service.specific.inter.spec_cache.CaptchaCache;
import com.leo.elib.service.specific.inter.spec_cache.VerifyCodeCache;
import com.leo.elib.usecase.inter.verify.AuthenCodeGenerator;
import com.leo.elib.usecase.inter.verify.AuthenCodeManager;
import jakarta.annotation.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class AuthenCodeManagerImpl implements AuthenCodeManager {

  @Resource
  private AuthenCodeGenerator authenCodeGenerator;
  @Resource
  private VerifyCodeCache verifyCodeCache;
  @Resource
  private CaptchaCache captchaCache;

  @Override
  public String createAndCacheVerifyCode(String contact, AuthenCodeType type) {
    String code = authenCodeGenerator.generateVerifyCode();
    verifyCodeCache.storeCode(contact, type, code);
    return code;
  }

  @Override
  public Pair<String, String> createAndCacheCaptchaCode(String ip, long timestamp) {
    String code = authenCodeGenerator.generateCaptchaCode();
    String key = captchaCache.storeCaptchaCode(ip, timestamp, code);
    return Pair.of(key, code);
  }

  @Override
  public boolean verifyCaptchaCode(String key, String code) {
    return captchaCache.verifyCaptchaCode(key, code);
  }

  @Override
  public boolean verifyVerifyCode(String contact, AuthenCodeType type, String code) {
    return verifyCodeCache.verifyCode(contact, type, code);
  }

}