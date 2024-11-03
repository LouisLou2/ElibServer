package com.leo.elib.usecase.inter.verify;

import com.leo.elib.constant.AuthenCodeType;
import org.springframework.data.util.Pair;

public interface AuthenCodeManager {
  // for email
  String createAndCacheVerifyCode(String contact, AuthenCodeType type);
  // for captcha
  Pair<String,String> createAndCacheCaptchaCode(String ip, long timestamp);
  boolean verifyCaptchaCode(String key, String code);
  boolean verifyVerifyCode(String contact, AuthenCodeType type, String code);
}
