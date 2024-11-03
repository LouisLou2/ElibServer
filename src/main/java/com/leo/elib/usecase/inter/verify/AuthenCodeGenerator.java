package com.leo.elib.usecase.inter.verify;

public interface AuthenCodeGenerator {
  String generateCaptchaCode();
  String generateVerifyCode();
}
