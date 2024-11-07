package com.leo.elib.service.specific.impl;

import com.leo.elib.config.ServiceConstConfig;
import com.leo.elib.service.base_service.inter.RCacheManager;
import com.leo.elib.service.specific.inter.EmailContentProvider;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmailContentProviderImpl implements EmailContentProvider {

  @Value("${container.redis.template.content-hash}")
  private String contentCacheContainer;
  private String verifyCodeEmailContKey = "verify_code_email"; // 邮箱验证码模板key
  private String verifyCodeEmailContIndexKey = "verify_code_email_index";// 邮箱验证码字符串嵌入位置key
  private String needUpdateEmailContKey = "need_update_email";

  private String verifyCodeEmailTemplate;
  private int verifyCodeEmailIndex;
  private boolean needUpdate;

  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> opsForHash;

  @PostConstruct
  private void init() {
    opsForHash = rCacheManager.getOpsForHash();
  }

  private synchronized void updateEmailCodeTemplate() {
    if (!needUpdate) {
      return;
    }
    List<Object> res = opsForHash.multiGet(
      contentCacheContainer,
      List.of(verifyCodeEmailContKey, verifyCodeEmailContIndexKey)
    );
    assert res.size() == 2;
    verifyCodeEmailTemplate = (String) res.get(0);
    verifyCodeEmailIndex = (int) res.get(1);
    needUpdate = false;
    // 更新缓存
    opsForHash.put(contentCacheContainer, needUpdateEmailContKey, false);
  }

  @Override
  public String getEmailCodeTemForLogin(String code) {
    assert code != null && code.length() == ServiceConstConfig.EmailVerifyCodeLength;
    // 去缓存中获取是否要更新,实际是一定要求不为null
    var needUpdateRes = opsForHash.get(contentCacheContainer,needUpdateEmailContKey);
    assert needUpdateRes != null;
    needUpdate = (boolean) needUpdateRes;
    needUpdate = true; //TODO: debug时才需要，正式环境需要注释掉
    if (needUpdate){
      updateEmailCodeTemplate();
    }
    StringBuilder sb = new StringBuilder(verifyCodeEmailTemplate);
    sb.replace(
      verifyCodeEmailIndex,
      verifyCodeEmailIndex + ServiceConstConfig.EmailVerifyCodeLength,
      code
    );
    return sb.toString();
  }
}
