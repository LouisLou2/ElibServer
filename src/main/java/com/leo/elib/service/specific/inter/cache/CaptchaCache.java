package com.leo.elib.service.specific.inter.cache;

public interface CaptchaCache {
  // 返回key
  String storeCaptchaCode(String ip, long timestamp, String code);
  boolean verifyCaptchaCode(String key, String code);
}
