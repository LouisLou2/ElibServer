package com.leo.elib.usecase.impl;

import com.leo.elib.constant.AuthenCodeType;
import com.leo.elib.service.inter.EmailSevice;
import com.leo.elib.service.specific.inter.EmailContentProvider;
import com.leo.elib.usecase.inter.Notifier;
import com.leo.elib.usecase.inter.verify.AuthenCodeManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NotifierImpl implements Notifier{
  @Resource
  private AuthenCodeManager authenCodeManager;
  @Resource
  private EmailContentProvider emailContentProvider;
  @Resource
  private EmailSevice emailSevice;

  @Override
  public void sendAndNoteEmailVerifyCode(String email) {
    String code = authenCodeManager.createAndCacheVerifyCode(email, AuthenCodeType.Email);
    String content = emailContentProvider.getEmailCodeTemForLogin(code);
    // send email
    emailSevice.sendHtmlEmail(email, "验证码", content);
  }
}