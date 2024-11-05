package com.leo.elib.service.specific.inter.spec_cache;

import com.leo.elib.constant.AuthenCodeType;

public interface VerifyCodeCache {
  void storeCode(String contact, AuthenCodeType type, String code);
  // 相同与否，都不会删除缓存
  boolean verifyCode(String contact, AuthenCodeType type, String code);
}
